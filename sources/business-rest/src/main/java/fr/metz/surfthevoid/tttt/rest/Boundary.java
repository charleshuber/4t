package fr.metz.surfthevoid.tttt.rest;

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

import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

//Aucune transaction ne doit être commencée 
//au moment de l'accès au webservice
@Transactional(TxType.NEVER)
public abstract class Boundary {
	
	protected Log log = LogFactory.getLog(getClass());
	
	/**
	 * Méthode générique de création d'une ressource
	 */
	
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
