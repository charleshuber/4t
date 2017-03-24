package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.inject.Inject;
import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;

@Named("userBoundary")
public class UserBoundary extends ResourceBoundary<User> implements IUserBoundary{
	
	@Inject
	protected UserStore userStore;

	@Override
	protected ResourceStore<User, UserDbo> getStore() {
		return userStore;
	}
}