package fr.metz.surfthevoid.tttt.rest.resources;

import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class ResourceBoundary<T extends Resource> implements IResourceBoundary<T> {

	@Override
	public Response create(T resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response update(T resource) {
		// TODO Auto-generated method stub
		return null;
	}

}
