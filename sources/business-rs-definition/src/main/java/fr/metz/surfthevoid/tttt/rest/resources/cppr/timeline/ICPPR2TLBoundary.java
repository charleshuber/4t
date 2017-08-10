package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;

@Path("/compiledPeriod/timeline")
public interface ICPPR2TLBoundary extends IResourceBoundary<CPPR2TL>{
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response all();
	
	
	@GET
	@Path("/all/{cpprId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response allOfCPPR(@PathParam("cpprId") Long id);
	
}
