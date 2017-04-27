package fr.metz.surfthevoid.tttt.rest.resources;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.security.access.AccessDeniedException;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

@Transactional(TxType.NEVER)
public abstract class ResourceBoundary<R extends Resource>{
	
	protected Log log = LogFactory.getLog(getClass());

	public Response create(R resource) {
		OperationalInterface<R> operation = input -> getStore().create(input);
		return doOperate(resource, Status.CREATED, operation);
	}

	public Response read(Long id) {
		OperationalInterface<Long> operation = input -> getStore().read(id);
		return doOperate(id, Status.OK, operation);
	}
	
	public Response update(R resource) {
		OperationalInterface<R> operation = input -> getStore().update(input);
		return doOperate(resource, Status.OK, operation);
	}
	
	public Response delete(Long id) {
		OperationalInterface<Long> operation = input -> getStore().delete(id);
		return doOperate(id, Status.OK, operation);
	}
	
	public <T> Response doOperate(T input, Status successStatus, OperationalInterface<T> operation){
		ResponseBuilder rb = new ResponseBuilderImpl();
		try {
			Object entity = operation.operate(input);
			rb.status(successStatus).entity(entity);
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
	
	protected abstract ResourceStore<R, ? extends GenericDbo> getStore();
	
	@FunctionalInterface
	private static interface OperationalInterface<T> {
		public Object operate(T arg) throws ValidationException;  
	}
}
