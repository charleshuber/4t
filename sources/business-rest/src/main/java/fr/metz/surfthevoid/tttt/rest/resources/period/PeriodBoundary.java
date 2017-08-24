package fr.metz.surfthevoid.tttt.rest.resources.period;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;

/**
 * Rest access point to manipulate compiled periods resources
 */
@Named("periodBoundary")
public class PeriodBoundary extends ResourceBoundary<Period> implements IPeriodBoundary{
	
	@Inject
	protected PeriodStore periodStore;
	
	@Override
	protected PeriodStore getStore() {
		return periodStore;
	}

	@Override
	public Response all() {
		ReadSetInterface<Period> operation = () -> periodStore.readAll();
		return readSet(operation);
	}

	@Override
	public Response allOfTimeline(Long tlid) {
		ReadSetInterface<Period> readSetAction = () -> periodStore.allOfTimeline(tlid);
		return readSet(readSetAction);
	}

}
