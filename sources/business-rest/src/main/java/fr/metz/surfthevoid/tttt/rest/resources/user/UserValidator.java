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
		validatePwd(input, op, errors);
		validateFirstname(input, op, errors);
		validateLastname(input, op, errors);
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
	
	protected void validatePwd(User input, Operation op, Errors errors){
		if(op == Operation.CREATE){
			validatePwdAtCreation(input, errors);
		} else if(op == Operation.UDPDATE && StringUtils.isEmpty(input.getNewPassword())){
			validatePwdAtUpdate(input, errors);
		}
	}

	protected void validatePwdAtCreation(User input, Errors errors) {
		Boolean oneEmpty = false;
		if(StringUtils.isEmpty(input.getNewPassword())){
			errors.addFieldError(User.NEW_PASSWORD_FIELD_NAME, UserValidationErrors.PWD_IS_MANDATORY.getCode());
			oneEmpty = true;
		}
		if(StringUtils.isEmpty(input.getNewPasswordCheck())){
			errors.addFieldError(User.NEW_PASSWORD_CHECK_FIELD_NAME, UserValidationErrors.PWD_CHECK_IS_MANDATORY.getCode());
			oneEmpty = true;
		}
		if(!oneEmpty && !input.getNewPassword().equals(input.getNewPasswordCheck())){
			errors.addFieldError(User.NEW_PASSWORD_CHECK_FIELD_NAME, UserValidationErrors.PWD_CHECK_IS_NOT_VALID.getCode());
		}
	}
	
	protected void validatePwdAtUpdate(User input, Errors errors) {
		if(StringUtils.isEmpty(input.getNewPasswordCheck())){
			errors.addFieldError(User.NEW_PASSWORD_CHECK_FIELD_NAME, UserValidationErrors.PWD_CHECK_IS_MANDATORY.getCode());
		} else if(!input.getNewPassword().equals(input.getNewPasswordCheck())){
			errors.addFieldError(User.NEW_PASSWORD_CHECK_FIELD_NAME, UserValidationErrors.PWD_CHECK_IS_NOT_VALID.getCode());
		}
	}
	
	protected void validateLastname(User input, Operation op, Errors errors){
		if(StringUtils.isEmpty(input.getLastName())){
			errors.addFieldError(User.LASTNAME_FIELD_NAME, UserValidationErrors.LASTNAME_IS_MANDATORY.getCode());
		}
	}
	
	protected void validateFirstname(User input, Operation op, Errors errors){
		if(StringUtils.isEmpty(input.getFirstName())){
			errors.addFieldError(User.FIRSTNAME_FIELD_NAME, UserValidationErrors.FIRSTNAME_IS_MANDATORY.getCode());
		}
	}

	@Override
	protected UserDao getDao() {
		return dao;
	}
	
}
