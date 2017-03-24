package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.inject.Inject;
import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

@Named
public class UserValidator extends Validator<User, UserDbo>{
	
	@Inject
	protected UserDao dao;
	
	@Override
	public void validateInput(User input, Operation op, Errors errors) throws ValidationException {
		// TODO Auto-generated method stub		
	}

	@Override
	protected UserDao getDao() {
		return dao;
	}
	
}