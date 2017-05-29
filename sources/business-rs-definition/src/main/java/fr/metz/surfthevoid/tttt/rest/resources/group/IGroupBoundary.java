package fr.metz.surfthevoid.tttt.rest.resources.group;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceListBoundary;

@Path("/group")
public interface IGroupBoundary extends IResourceListBoundary<Group>{
	
	@GET
	@Path("/children/{groupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response children(@PathParam("groupId") Integer groupId);
	
	
}
