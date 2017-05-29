package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.inject.Inject;
import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.resources.ListStore;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceListBoundary;

/**
 * Rest access point to manipulate user resources
 */
@Named("userBoundary")
public class UserBoundary extends ResourceListBoundary<User> implements IUserBoundary{
	
	@Inject
	protected UserStore userStore;

	@Override
	protected ListStore<User, UserDbo> getStore() {
		return userStore;
	}
}
