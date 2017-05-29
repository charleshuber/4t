package fr.metz.surfthevoid.tttt.rest.resources.group;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.GroupDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GroupDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

@Named
public class GroupValidator extends Validator<Group, GroupDbo>{
	
	@Inject
	protected GroupDao dao;
	
	@Override
	public void validateInput(Group input, Operation op, Errors errors) throws ValidationException {
		validatename(input, op, errors);	
	}

	protected void validatename(Group input, Operation op, Errors errors) {
		if(StringUtils.isEmpty(input.getName())){
			errors.addFieldError(Group.NAME_FIELD_NAME, GroupValidationErrors.NAME_IS_MANDATORY.getCode());
		} else {
			GroupDbo anyGroup = dao.readByName(input.getName());
			if(op == Operation.CREATE && anyGroup != null){
				errors.addFieldError(Group.NAME_FIELD_NAME, GroupValidationErrors.NAME_IS_ALREADY_USED.getCode());
			} else if(op == Operation.UDPDATE && (anyGroup == null || anyGroup.getId() != input.getId())){
				errors.addFieldError(Group.NAME_FIELD_NAME, GroupValidationErrors.NAME_IS_ALREADY_USED.getCode());
			}	
		}
	}

	@Override
	protected GroupDao getDao() {
		return dao;
	}
	
}
