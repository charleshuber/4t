package fr.metz.surfthevoid.tttt.rest.resources.cppr;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

@Named
public class CompiledPeriodValidator extends Validator<CompiledPeriod, CompiledPeriodDbo>{
	
	@Inject
	protected CompiledPeriodDao dao;
	
	@Override
	public void validateInput(CompiledPeriod input, Operation op, Errors errors) throws ValidationException {
		validatename(input, op, errors);	
	}

	protected void validatename(CompiledPeriod input, Operation op, Errors errors) {
		if(StringUtils.isEmpty(input.getName())){
			errors.addFieldError(CompiledPeriod.NAME_FIELD_NAME, CompiledPeriodValidationErrors.NAME_IS_MANDATORY.getCode());
		} else {
			CompiledPeriodDbo anyCPPR = dao.readByName(input.getName());
			if(op == Operation.CREATE && anyCPPR != null){
				errors.addFieldError(CompiledPeriod.NAME_FIELD_NAME, CompiledPeriodValidationErrors.NAME_IS_ALREADY_USED.getCode());
			} else if(op == Operation.UDPDATE && (anyCPPR != null && anyCPPR.getId() != input.getId())){
				errors.addFieldError(CompiledPeriod.NAME_FIELD_NAME, CompiledPeriodValidationErrors.NAME_IS_ALREADY_USED.getCode());
			}	
		}
	}

	@Override
	protected CompiledPeriodDao getDao() {
		return dao;
	}
	
}
