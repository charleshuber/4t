package fr.metz.surfthevoid.tttt.rest.resources;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;

@Transactional(TxType.NEVER)
public abstract class ResourceBoundary<R extends Resource> implements IResourceBoundary<R> {

	@Override
	public Response create(R resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response update(R resource) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Response read(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected abstract ResourceStore<R, ? extends GenericDbo> getResourceStore();

}
