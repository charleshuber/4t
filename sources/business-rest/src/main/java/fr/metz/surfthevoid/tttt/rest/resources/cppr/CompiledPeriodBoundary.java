package fr.metz.surfthevoid.tttt.rest.resources.cppr;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;

/**
 * Rest access point to manipulate compiled periods resources
 */
@Named("cpprBoundary")
public class CompiledPeriodBoundary extends ResourceBoundary<CompiledPeriod> implements ICompiledPeriodBoundary{
	
	@Inject
	protected CompiledPeriodStore cpprStore;
	
	@Override
	protected CompiledPeriodStore getStore() {
		return cpprStore;
	}

	@Override
	public Response all() {
		ReadSetInterface<CompiledPeriod> operation = () -> cpprStore.readAll();
		return readSet(operation);
	}

	@Override
	public Response allOfTimeline(Long tlid) {
		ReadSetInterface<CompiledPeriod> readSetAction = () -> cpprStore.allOfTimeline(tlid);
		return readSet(readSetAction);
	}

}
