package fr.metz.surfthevoid.tttt.rest.resources.cronperiod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;

@Path("/cronperiods")
public interface ICronPeriodBoundary extends IResourceBoundary<CronPeriod>{
	
	@GET
	@Path("/timeline/{tlid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response allOfTimeline(@PathParam("tlid") Long tlid);
		
}
