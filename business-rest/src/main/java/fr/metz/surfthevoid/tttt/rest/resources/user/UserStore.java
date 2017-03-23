package fr.metz.surfthevoid.tttt.rest.resources.user;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;

@Named
public class UserStore extends ResourceStore<User, UserDbo>{
	
	@Inject
	protected UserDao dao;
	
	@Inject
	protected UserValidator validator;
	
	@Override
	protected UserDao getDao() {
		return dao;
	}

	@Override
	protected UserValidator getValidator() {
		return validator;
	}
	
	public Set<User> readAll(){
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toSet());
	}

	@Override
	protected UserDbo transform(User res) {
		UserDbo userDbo = new UserDbo();
		userDbo.setId(res.getId());
		userDbo.setEmail(res.getEmail());
		userDbo.setPassword(res.getPassword());
		userDbo.setFirstName(res.getFirstName());
		userDbo.setLastName(res.getLastName());
		return userDbo;
	}

	@Override
	protected User extract(UserDbo dbo) {
		User user = new User();
		user.setId(dbo.getId());
		user.setEmail(dbo.getEmail());
		user.setPassword(dbo.getPassword());
		user.setFirstName(dbo.getFirstName());
		user.setLastName(dbo.getLastName());
		return user;
	}
}
