package fr.metz.surfthevoid.tttt.rest.resources.period;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;

@Path("/period")
public interface IPeriodBoundary extends IResourceBoundary<Period>{
	
	@GET
	@Path("/timeline/{tlid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response allOfTimeline(@PathParam("tlid") Long tlid);
		
}
