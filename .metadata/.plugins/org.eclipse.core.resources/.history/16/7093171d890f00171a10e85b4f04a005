package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.inject.Inject;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

public class UserValidator extends Validator<User, UserDbo>{
	
	@Inject
	protected UserDao dao;
	
	@Override
	public void validateInput(User input, Operation op) throws ValidationException {
		// TODO Auto-generated method stub		
	}

	@Override
	protected UserDao getDao() {
		return dao;
	}
	
}
