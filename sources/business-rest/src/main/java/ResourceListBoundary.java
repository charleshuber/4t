import java.util.Set;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.security.access.AccessDeniedException;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.ListStore;
import fr.metz.surfthevoid.tttt.rest.resources.Resource;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

@Transactional(TxType.NEVER)
public abstract class ResourceListBoundary<R extends Resource> extends ResourceBoundary<R> {
	
	public Response readAll() {
		ResponseBuilder rb = new ResponseBuilderImpl();
		try {
			Set<R> entity = getStore().readAll();
			rb.status(Status.OK).entity(new GenericEntity<Set<R>>(entity, Set.class));
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
	
	@Override
	protected abstract ListStore<R, ? extends GenericDbo> getStore();
}
