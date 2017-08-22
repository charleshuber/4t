package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CPPR2TLDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CPPR2TLDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimeLineDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

@Named
public class CPPR2TLValidator extends Validator<CPPR2TL, CPPR2TLDbo>{
	
	@Inject
	protected CPPR2TLDao dao;
	
	@Inject
	protected CompiledPeriodDao cpprDao;
	
	@Inject
	protected TimeLineDao tlDao;
	
	@Override
	public void validateInput(CPPR2TL input, Operation op, Errors errors) throws ValidationException {
		validateCPPR(input, op, errors);
		validateTimeline(input, op, errors);
	}

	protected void validateCPPR(CPPR2TL input, Operation op, Errors errors) {
		if(StringUtils.isEmpty(input.getName())){
			errors.addFieldError(CPPR2TL.NAME_FIELD_NAME, CPPR2TLValidationErrors.NAME_IS_MANDATORY.getCode());
		} else {
			CPPR2TLDbo anyCPPR = dao.readByName(input.getName());
			if(op == Operation.CREATE && anyCPPR != null){
				errors.addFieldError(CPPR2TL.NAME_FIELD_NAME, CPPR2TLValidationErrors.NAME_IS_ALREADY_USED.getCode());
			} else if(op == Operation.UDPDATE && (anyCPPR != null && anyCPPR.getId() != input.getId())){
				errors.addFieldError(CPPR2TL.NAME_FIELD_NAME, CPPR2TLValidationErrors.NAME_IS_ALREADY_USED.getCode());
			}	
		}
	}
	
	protected void validateTimeline(CPPR2TL input, Operation op, Errors errors) {
		
	}
	
	protected void validateOrder(CPPR2TL input, Operation op, Errors errors) {
	
	}

	@Override
	protected CPPR2TLDao getDao() {
		return dao;
	}
	
}
