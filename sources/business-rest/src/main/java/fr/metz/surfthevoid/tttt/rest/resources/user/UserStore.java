package fr.metz.surfthevoid.tttt.rest.resources.user;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;
import fr.metz.surfthevoid.tttt.rest.resources.group.Group;
import fr.metz.surfthevoid.tttt.rest.resources.group.GroupStore;

@Named
public class UserStore extends ResourceStore<User, UserDbo>{
	
	@Inject
	protected UserDao dao;
	
	@Inject
	protected GroupStore groupStore;
	
	@Inject
	protected UserValidator validator;
	
	public Set<Group> getGroups(Long id) throws ValidationException {
		UserDbo dbUser =  dao.read(id);
		if(dbUser != null){
			if(CollectionUtils.isNotEmpty(dbUser.getGroups())){
				return dbUser.getGroups().stream()
				.map(dbGroup -> groupStore.extract(dbGroup))
				.collect(Collectors.toSet());
			}
			return new HashSet<Group>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	@Override
	protected UserDao getDao() {
		return dao;
	}

	@Override
	protected UserValidator getValidator() {
		return validator;
	}
	
	public Set<User> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toSet());
	}

	@Override
	protected UserDbo transform(User res) {
		UserDbo userDbo = new UserDbo();
		userDbo.setId(res.getId());
		userDbo.setEmail(res.getEmail());
		if(StringUtils.isNotEmpty(res.getNewPassword())){
			String encodedPassword = getEncoder().encodePassword(res.getNewPassword(), null);
			userDbo.setPassword(encodedPassword);
		}
		userDbo.setFirstName(res.getFirstName());
		userDbo.setLastName(res.getLastName());
		return userDbo;
	}

	@Override
	protected User extract(UserDbo dbo) {
		User user = new User();
		user.setId(dbo.getId());
		user.setEmail(dbo.getEmail());
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
			if(StringUtils.isNotEmpty(res.getNewPassword())){
				res.setNewPassword(res.getNewPassword().trim());
			}
			if(StringUtils.isNotEmpty(res.getNewPasswordCheck())){
				res.setNewPasswordCheck(res.getNewPasswordCheck().trim());
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
	
	protected MessageDigestPasswordEncoder getEncoder(){
		return new Md5PasswordEncoder();
	}
}
