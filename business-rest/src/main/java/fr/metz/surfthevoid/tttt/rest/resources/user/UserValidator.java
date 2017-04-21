package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

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
		validateEmail(input, op, errors);	
	}

	protected void validateEmail(User input, Operation op, Errors errors) {
		if(StringUtils.isEmpty(input.getEmail())){
			errors.addFieldError(User.EMAIL_FIELD_NAME, UserValidationErrors.EMAIL_IS_MANDATORY.getCode());
		} else {
			UserDbo anyuser = dao.readByMail(input.getEmail());
			if(op == Operation.CREATE && anyuser != null){
				errors.addFieldError(User.EMAIL_FIELD_NAME, UserValidationErrors.EMAIL_IS_ALREADY_USED.getCode());
			} else if(op == Operation.UDPDATE && (anyuser == null || anyuser.getId() != input.getId())){
				errors.addFieldError(User.EMAIL_FIELD_NAME, UserValidationErrors.EMAIL_IS_ALREADY_USED.getCode());
			}	
		}
	}

	@Override
	protected UserDao getDao() {
		return dao;
	}
	
}
