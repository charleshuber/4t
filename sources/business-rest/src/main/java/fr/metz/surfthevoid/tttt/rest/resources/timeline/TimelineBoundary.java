package fr.metz.surfthevoid.tttt.rest.resources.timeline;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;

/**
 * Rest access point to manipulate compiled periods resources
 */
@Named("timelineBoundary")
public class TimelineBoundary extends ResourceBoundary<Timeline> implements ITimelineBoundary{
	
	@Inject
	protected TimelineStore timelineStore;
	
	@Override
	protected TimelineStore getStore() {
		return timelineStore;
	}

	@Override
	public Response all() {
		ReadSetInterface<Timeline> operation = () -> timelineStore.readAll();
		return readSet(operation);
	}

	@Override
	public Response addCPPR(Long timelineId, Long cpprId) {
		return executePingAction(() -> timelineStore.addCPPR(timelineId, cpprId));
	}

	@Override
	public Response removeCPPR(Long timelineId, Long cpprId) {
		return executePingAction(() -> timelineStore.removeCPPR(timelineId, cpprId));
	}

	@Override
	public Response addCRPR(Long timelineId, Long crprId) {
		return executePingAction(() -> timelineStore.addCRPR(timelineId, crprId));
	}

	@Override
	public Response removeCRPR(Long timelineId, Long crprId) {
		return executePingAction(() -> timelineStore.removeCRPR(timelineId, crprId));
	}

	@Override
	public Response addPeriod(Long timelineId, Long periodId) {
		return executePingAction(() -> timelineStore.addPeriod(timelineId, periodId));
	}

	@Override
	public Response removePeriod(Long timelineId, Long periodId) {
		return executePingAction(() -> timelineStore.removePeriod(timelineId, periodId));
	}
}
