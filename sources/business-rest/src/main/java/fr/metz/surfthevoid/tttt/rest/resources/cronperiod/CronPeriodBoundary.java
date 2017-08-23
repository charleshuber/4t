package fr.metz.surfthevoid.tttt.rest.resources.cronperiod;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;

/**
 * Rest access point to manipulate compiled periods resources
 */
@Named("cpprBoundary")
public class CronPeriodBoundary extends ResourceBoundary<CronPeriod> implements ICronPeriodBoundary{
	
	@Inject
	protected CronPeriodStore cpprStore;
	
	@Override
	protected CronPeriodStore getStore() {
		return cpprStore;
	}

	@Override
	public Response all() {
		ReadSetInterface<CronPeriod> operation = () -> cpprStore.readAll();
		return readSet(operation);
	}

	@Override
	public Response allOfTimeline(Long tlid) {
		ReadSetInterface<CronPeriod> readSetAction = () -> cpprStore.allOfTimeline(tlid);
		return readSet(readSetAction);
	}

}
