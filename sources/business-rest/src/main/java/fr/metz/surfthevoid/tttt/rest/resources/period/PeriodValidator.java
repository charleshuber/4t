package fr.metz.surfthevoid.tttt.rest.resources.period;


import javax.inject.Inject;
import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.db.entity.PeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.PeriodDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

@Named
public class PeriodValidator extends Validator<Period, PeriodDbo>{
	
	@Inject
	protected PeriodDao dao;
	
	@Override
	public void validateInput(Period input, Operation op, Errors errors) throws ValidationException {
		if(input.getStartTime() == null){
			errors.addFieldError(Period.START_TIME_FIELD_NAME, PeriodValidationErrors.START_TIME_IS_MANDATORY.getCode());
		}
		if(input.getEndTime() == null){
			errors.addFieldError(Period.END_TIME_FIELD_NAME, PeriodValidationErrors.END_TIME_IS_MANDATORY.getCode());
		}
		if(input.getStartTime() != null 
				&& input.getEndTime() != null 
				&& !input.getEndTime().isAfter(input.getStartTime())){
			errors.addGlobalError(PeriodValidationErrors.PERIOD_IS_INVALID.getCode());
		}
	}

	@Override
	protected PeriodDao getDao() {
		return dao;
	}
	
}
