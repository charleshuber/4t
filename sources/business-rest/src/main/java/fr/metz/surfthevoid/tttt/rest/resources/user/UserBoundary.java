package fr.metz.surfthevoid.tttt.rest.resources.user;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.security.access.AccessDeniedException;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

/**
 * Rest access point to manipulate user resources
 */
@Named("userBoundary")
public class UserBoundary extends ResourceBoundary<User> implements IUserBoundary{
	
	@Inject
	protected UserStore userStore;

	@Override
	protected ResourceStore<User, UserDbo> getStore() {
		return userStore;
	}
	
	@Override
	public Response readAll(){
		ResponseBuilder rb = new ResponseBuilderImpl();
		try {
			Set<User> entity = userStore.readAll();
			rb.status(Status.OK).entity(new GenericEntity<Set<User>>(entity, Set.class));
		} catch (ValidationException e) {
			if(e.getType() == Type.INVALID_RIGHT){
				rb.status(Status.FORBIDDEN);
			} else if(e.getType() == Type.NO_CONTENT){
				rb.status(Status.NO_CONTENT);
			} else if(e.getType() == Type.CONFLICT){
				rb.status(Status.CONFLICT);
			} else {
				rb.status(Status.BAD_REQUEST);
			}
			if(e.hasErrors()){
				rb.header(IResourceBoundary.VALIDATION_HEADER, true);
				rb.entity(e.getErrors());
			}
		} catch(AccessDeniedException e){
			rb.status(Status.FORBIDDEN);
		} catch (Exception e){
			log.error(e);
			rb.status(Status.INTERNAL_SERVER_ERROR);
		}
		return rb.build();
	}
}
