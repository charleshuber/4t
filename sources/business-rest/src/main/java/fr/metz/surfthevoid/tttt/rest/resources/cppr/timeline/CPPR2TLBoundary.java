package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;

/**
 * Rest access point to manipulate compiled periods resources
 */
@Named("cppr2tlBoundary")
public class CPPR2TLBoundary extends ResourceBoundary<CPPR2TL> implements ICPPR2TLBoundary{
	
	@Inject
	protected CPPR2TLStore cppr2tlStore;
	
	@Override
	protected CPPR2TLStore getStore() {
		return cppr2tlStore;
	}

	@Override
	public Response all() {
		ReadSetInterface<CPPR2TL> operation = () -> cppr2tlStore.readAll();
		return readSet(operation);
	}

	@Override
	public Response allOfCPPR(Long id) {
		ReadSetInterface<CPPR2TL> readSetAction = () -> cppr2tlStore.allOfCPPR(id);
		return readSet(readSetAction);
	}

	

}
