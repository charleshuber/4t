package fr.metz.surfthevoid.tttt.rest.resources.timeline;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceBoundary;

@Path("/timeline")
public interface ITimelineBoundary extends IResourceBoundary<Timeline>{
	
	@GET
	@Path("/{tlId}/add/compiledPeriod/{cpprId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCPPR(@PathParam("tlId") Long timelineId, @PathParam("cpprId") Long cpprId);
	
	@GET
	@Path("/{tlId}/remove/compiledPeriod/{cpprId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCPPR(@PathParam("tlId") Long timelineId, @PathParam("cpprId") Long cpprId);
	
	@GET
	@Path("/{tlId}/add/cronPeriod/{cpprId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCRPR(@PathParam("tlId") Long timelineId, @PathParam("crprId") Long crprId);
	
	@GET
	@Path("/{tlId}/remove/cronPeriod/{cpprId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCRPR(@PathParam("tlId") Long timelineId, @PathParam("crprId") Long crprId);
	
	@GET
	@Path("/{tlId}/add/period/{periodId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPeriod(@PathParam("tlId") Long timelineId, @PathParam("periodId") Long periodId);
	
	@GET
	@Path("/{tlId}/remove/period/{periodId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removePeriod(@PathParam("tlId") Long timelineId, @PathParam("periodId") Long periodId);
}
