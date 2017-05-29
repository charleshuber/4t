package fr.metz.surfthevoid.tttt.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface IResourceListBoundary <R extends Resource> extends IResourceBoundary<R>{
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAll();
}
