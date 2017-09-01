package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;

@Path("/user")
public interface IUserBoundary extends IResourceBoundary<User>{
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response all();
	
	@GET
	@Path("allOfGroup/{groupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response allOfGroup(@PathParam("groupId") Long groupId);
	
}
