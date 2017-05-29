package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;
import fr.metz.surfthevoid.tttt.rest.resources.ListStore;

@Named
public class UserStore extends ListStore<User, UserDbo>{
	
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
