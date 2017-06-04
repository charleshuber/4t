package fr.metz.surfthevoid.tttt.rest.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ResourceBoundaryTest {

	@Test
	public void createTest() {		
		ResourceBoundary boundary = Mockito.mock(ResourceBoundary.class);
		Mockito.when(boundary.create(Mockito.any(Resource.class))).thenCallRealMethod();
		Resource resource = Mockito.mock(Resource.class);
		boundary.create(resource);
	}
	
	/*
	 * public Response create(R resource) {
		OperationalInterface<R> operation = input -> getStore().create(input);
		return doOperate(resource, Status.CREATED, operation);
	}
	 */
}
