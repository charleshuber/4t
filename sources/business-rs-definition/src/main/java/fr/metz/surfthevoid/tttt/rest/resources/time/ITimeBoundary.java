package fr.metz.surfthevoid.tttt.rest.resources.time;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/time")
public interface ITimeBoundary {
	
	@GET
	@Path("/timeline/{tlid}/compilation/{start}/{end}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response timelineCompilation(@PathParam("tlid") Long tlid, Date start, Date end);
	
	@GET
	@Path("/compiledPeriod/{cpprid}/compilation/{start}/{end}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cpprCompilation(@PathParam("cpprid") Long cpprid, Date start, Date end);
	
		
}
