package fr.metz.surfthevoid.tttt.rest.resources.user;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

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

	@Override
	protected User clean(User res) {
		if(res != null){
			if(StringUtils.isNotEmpty(res.getEmail())){
				res.setEmail(res.getEmail().trim());
			}
			if(StringUtils.isNotEmpty(res.getPassword())){
				res.setPassword(res.getPassword().trim());
			}
			if(StringUtils.isNotEmpty(res.getFirstName())){
				res.setFirstName(res.getFirstName().trim());
			}
			if(StringUtils.isNotEmpty(res.getLastName())){
				res.setLastName(res.getLastName().trim());
			}
		}
		return res;
	}
}
