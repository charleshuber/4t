package fr.metz.surfthevoid.tttt.rest.resources.user;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
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
				.collect(Collectors.toCollection(groupStore.getOrderedByName()));
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
				.collect(Collectors.toCollection(getOrderedById()));
	}

	@Override
	protected UserDbo transform(User res) {
		UserDbo userDbo = new UserDbo();
		userDbo.setId(res.getId());
		userDbo.setEmail(res.getEmail());
		userDbo.setPassword(res.getNewPassword());
		userDbo.setFirstName(res.getFirstName());
		userDbo.setLastName(res.getLastName());
		return userDbo;
	}

	@Override
	public User extract(UserDbo dbo) {
		User user = new User();
		user.setId(dbo.getId());
		user.setEmail(dbo.getEmail());
		user.setFirstName(dbo.getFirstName());
		user.setLastName(dbo.getLastName());
		return user;
	}

	@Override
	protected User clean(User res, Operation op) {
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
	
	@Override
	protected User postValidationActions(User res, Operation op){
		preparePassword(res, op);
		return res;
	}

	protected void preparePassword(User res, Operation op) {
		if(StringUtils.isNotEmpty(res.getNewPassword())){
			res.setNewPassword(encodePassword(res.getNewPassword()));
		} else if(op == Operation.UDPDATE){
			UserDbo dbUser = dao.read(res.getId());
			String notModifiedPwd = dbUser.getPassword();
			res.setNewPassword(notModifiedPwd);
		}
	}
	
	public String encodePassword(String password){
		if(StringUtils.isNotEmpty(password))
		return getEncoder().encodePassword(password, null);
		return StringUtils.EMPTY;
	}
	
	protected MessageDigestPasswordEncoder getEncoder(){
		return new Md5PasswordEncoder();
	}
	
	public Supplier<TreeSet<User>> getOrderedById(){
		return () -> new TreeSet<User>(Comparator.comparing(User::getId));
	}
}
