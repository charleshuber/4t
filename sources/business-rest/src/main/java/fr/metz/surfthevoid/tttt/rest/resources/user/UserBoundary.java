package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;

/**
 * Rest access point to manipulate user resources
 */
@Named("userBoundary")
public class UserBoundary extends ResourceBoundary<User> implements IUserBoundary{
	
	@Inject
	protected UserStore userStore;

	@Override
	protected UserStore getStore() {
		return userStore;
	}

	@Override
	public Response groups(Long id) {
		return readSet(() -> userStore.getGroups(id));
	}

	@Override
	public Response all() {
		return readSet(() -> userStore.readAll());
	}
}
