package fr.metz.surfthevoid.tttt.rest.resources;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.metz.surfthevoid.tttt.rest.Boundary;
import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;

/**
 * Classe abstraite d'accès au webservice
 * Elle concentre la totalité du code sur les accès au 4 methodes 
 * principales POST, GET, PUT, DELETE
 * @author charles
 *
 * @param <R>
 */
public abstract class ResourceBoundary<R extends Resource> extends Boundary implements IResourceBoundary<R> {
	
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
	 * Méthode abstraite que chaque classe héritière doit implémenter pour retourner le service en charge de gérer la ressource.
	 * Ce service doit forcément hériter de la classe abstraite ResourceStore qui implémente les quatres méthodes create, update, read et delete
	 * @return
	 */
	protected abstract ResourceStore<R, ? extends GenericDbo> getStore();
}
