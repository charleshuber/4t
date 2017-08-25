package fr.metz.surfthevoid.tttt.rest.resources;

import java.util.Set;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.security.access.AccessDeniedException;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

/**
 * Classe abstraite d'accès au webservice
 * Elle concentre la totalité du code sur les accès au 4 methodes 
 * principales POST, GET, PUT, DELETE
 * @author charles
 *
 * @param <R>
 */
// Aucune transaction ne doit être commencée 
// au moment de l'accès au webservice
@Transactional(TxType.NEVER)
public abstract class ResourceBoundary<R extends Resource> implements IResourceBoundary<R> {
	
	protected Log log = LogFactory.getLog(getClass());
	
	/**
	 * Méthode générique de création d'une ressource
	 */
	@Override
	public Response create(R resource) {
		// Construction du code a appeler dans la méthode d'encapsulation (récupération du service et appel de la méthode create)
		OperationalInterface<R> operation = input -> getStore().create(input);
		// Appel de la méthode d'encapsulation en précisant,
		// la fonction a exécuter et le code de retour HTTP en cas de succés
		return doOperate(resource, Status.CREATED, operation);
	}

	/**
	 * Méthode générique de lecture d'une ressource
	 */
	@Override
	public Response read(Long id) {
		// Construction du code a appeler dans la méthode d'encapsulation (récupération du service et appel de la méthode read)
		OperationalInterface<Long> operation = input -> getStore().read(input);
		// Appel de la méthode d'encapsulation en précisant,
		// la fonction a exécuter et le code de retour HTTP en cas de succés
		return doOperate(id, Status.OK, operation);
	}
	
	/**
	 * Méthode générique de mise à jour d'une ressource
	 */
	@Override
	public Response update(R resource) {
		// Construction du code a appeler dans la méthode d'encapsulation (récupération du service et appel de la méthode update)
		OperationalInterface<R> operation = input -> getStore().update(input);
		// Appel de la méthode d'encapsulation en précisant,
		// la fonction a exécuter et le code de retour HTTP en cas de succés
		return doOperate(resource, Status.OK, operation);
	}
	
	/**
	 * Méthode générique de suppression d'une ressource
	 */
	@Override
	public Response delete(Long id) {
		// Construction du code a appeler dans la méthode d'encapsulation (récupération du service et appel de la méthode delete)
		OperationalInterface<Long> operation = input -> getStore().delete(input);
		// Appel de la méthode d'encapsulation en précisant,
		// la fonction a exécuter et le code de retour HTTP en cas de succés
		return doOperate(id, Status.OK, operation);
	}
	
	/**
	 * Méthode générique permettant de factoriser le traitement, 
	 * dans le contexte d'un appel à l'une des 4 méthodes principales
	 * @param input
	 * @param successStatus
	 * @param operation
	 * @return
	 */
	public <T> Response doOperate(T input, Status successStatus, OperationalInterface<T> operation){
		// Construction de l'objet permettant de construire la réponse HTTP
		ResponseBuilder rb = new ResponseBuilderImpl();
		try {
			// Execution du code relatif à l'opération et récupération du résultat
			Object entity = operation.operate(input);
			// Définition du code de retour dans la réponse HTTP
			rb.status(successStatus).entity(entity);
		} catch (ValidationException e) {
			//gestion des erreurs de validation dans la construction de la réponse
			handleValidationException(rb, e);
		} catch(AccessDeniedException e){
			rb.status(Status.FORBIDDEN);
		} catch (Exception e){
			log.error(e);
			rb.status(Status.INTERNAL_SERVER_ERROR);
		}
		return rb.build();
	}
	
	/**
	 * Méthode générique permettant de factoriser le traitement, 
	 * dans le contexte d'un appel demandant une collection de ressources
	 * @param input
	 * @param successStatus
	 * @param operation
	 * @return
	 */
	public <L> Response readSet(ReadSetInterface<L> readCollectionInterface) {
		ResponseBuilder rb = new ResponseBuilderImpl();
		try {
			// Execution du code relatif à l'opération passer en paramètre et définition du statut de la réponse
			Set<L> entity = readCollectionInterface.readSet();
			rb.status(Status.OK).entity(new GenericEntity<Set<L>>(entity, Set.class));
		} catch (ValidationException e) {
			//gestion des erreurs de validation dans la construction de la réponse
			handleValidationException(rb, e);
		} catch(AccessDeniedException e){
			rb.status(Status.FORBIDDEN);
		} catch (Exception e){
			log.error(e);
			rb.status(Status.INTERNAL_SERVER_ERROR);
		}
		return rb.build();
	}
	
	/**
	 * Méthode générique permettant de factoriser le traitement ,
	 * dans le contexte d'un appel HTTP de type GET devant effectuer une action en base de donnée
	 * 
	 * Dans le cadre d'une application stricte de la philosophie REST, cette méthode ne devrait pas exister,
	 * mais le gain de temps est tel qu'il serait bête de s'en priver. 
	 * 
	 * Exemple:
	 * Si une ressource "Groupe" contient une liste d'"Utilisateurs",
	 * La principe du REST voudrait que pour ajouter ou supprimer un utilisateur du groupe,
	 * on doive mettre en place une ressource de type "LienEntreUnGroupeEtUnUtilisateur" et utiliser une des méthodes vue précedemment.
	 * 
	 * Dans la pratique il est plus simple d'appeler une URL qui contient les identifiants du groupe et de l'utilisateur que l'on veut lier ou délier.
	 * C'est ce à quoi sert cette fonction
	 * 
	 * @param input
	 * @param successStatus
	 * @param operation
	 * @return
	 */
	public Response executePingAction(PingActionInterface pingActionInterface) {
		ResponseBuilder rb = new ResponseBuilderImpl();
		try {
			// Execution du code relatif à l'opération passer en paramètre et définition du statut de la réponse
			pingActionInterface.pingAction();
			rb.status(Status.OK);
		} catch (ValidationException e) {
			//gestion des erreurs de validation dans la construction de la réponse
			handleValidationException(rb, e);
		} catch(AccessDeniedException e){
			rb.status(Status.FORBIDDEN);
		} catch (Exception e){
			log.error(e);
			rb.status(Status.INTERNAL_SERVER_ERROR);
		}
		return rb.build();
	}
	
	/**
	 * Gestion des erreurs de validation dans la construction de la réponse
	 * @param rb
	 * @param e
	 */
	protected void handleValidationException(ResponseBuilder rb, ValidationException e) {
		//On met à jour le status de la réponse en fonction du type d'erreur de validation
		if(e.getType() == Type.INVALID_RIGHT){
			rb.status(Status.FORBIDDEN);
		} else if(e.getType() == Type.NO_CONTENT){
			rb.status(Status.NO_CONTENT);
		} else if(e.getType() == Type.CONFLICT){
			rb.status(Status.CONFLICT);
		} else {
			rb.status(Status.BAD_REQUEST);
		}
		//Si il y a des messages d'erreur on les joint à la réponse en tant qu'objets JSON
		if(e.hasErrors()){
			rb.header(IResourceBoundary.VALIDATION_HEADER, true);
			rb.entity(e.getErrors());
		}
	}
	
	/**
	 * Méthode abstraite que chaque classe héritière doit implémenter pour retourner le service en charge de gérer la ressource.
	 * Ce service doit forcément hériter de la classe abstraite ResourceStore qui implémente les quatres méthodes create, update, read et delete
	 * @return
	 */
	protected abstract ResourceStore<R, ? extends GenericDbo> getStore();
	
	@FunctionalInterface
	protected static interface OperationalInterface<T> {
		public Object operate(T arg) throws ValidationException;  
	}
	
	@FunctionalInterface
	protected static interface ReadSetInterface<L> {
		public Set<L> readSet() throws ValidationException;  
	}
	
	@FunctionalInterface
	protected static interface PingActionInterface {
		public void pingAction() throws ValidationException;  
	}
}
