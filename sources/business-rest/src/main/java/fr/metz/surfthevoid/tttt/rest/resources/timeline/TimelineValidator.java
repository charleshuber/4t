package fr.metz.surfthevoid.tttt.rest.resources.timeline;


import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimelineDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

@Named
public class TimelineValidator extends Validator<Timeline, TimelineDbo>{
	
	@Inject
	protected TimelineDao dao;
	
	@Override
	public void validateInput(Timeline input, Operation op, Errors errors) throws ValidationException {
		validatename(input, op, errors);	
	}

	protected void validatename(Timeline input, Operation op, Errors errors) {
		if(StringUtils.isEmpty(input.getName())){
			errors.addFieldError(Timeline.NAME_FIELD_NAME, TimelineValidationErrors.NAME_IS_MANDATORY.getCode());
		} else {
			TimelineDbo anyTL = dao.readByName(input.getName());
			if(op == Operation.CREATE && anyTL != null){
				errors.addFieldError(Timeline.NAME_FIELD_NAME, TimelineValidationErrors.NAME_IS_ALREADY_USED.getCode());
			} else if(op == Operation.UDPDATE && (anyTL != null && anyTL.getId() != input.getId())){
				errors.addFieldError(Timeline.NAME_FIELD_NAME, TimelineValidationErrors.NAME_IS_ALREADY_USED.getCode());
			}	
		}
	}

	@Override
	protected TimelineDao getDao() {
		return dao;
	}
	
}
