package fr.metz.surfthevoid.tttt.rest.resources.group;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;

@Path("/group")
public interface IGroupBoundary extends IResourceBoundary<Group>{
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response all();
	
	@GET
	@Path("/{groupId}/children")
	@Produces(MediaType.APPLICATION_JSON)
	public Response children(@PathParam("groupId") Long groupId);
	
	@GET
	@Path("/{groupId}/parents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response parents(@PathParam("groupId") Long groupId);
	
	@GET
	@Path("/{groupId}/addChild/{childId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addChild(@PathParam("groupId") Long groupId, @PathParam("childId") Long childId);
	
	@GET
	@Path("/{groupId}/removeChild/{childId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeChild(@PathParam("groupId") Long groupId, @PathParam("childId") Long childId);
	
	@GET
	@Path("/{groupId}/addUser/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(@PathParam("groupId") Long groupId, @PathParam("userId") Long userId);
	
	@GET
	@Path("/{groupId}/removeUser/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeUser(@PathParam("groupId") Long groupId, @PathParam("userId") Long userId);
		
}
