package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

import javax.inject.Inject;
import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.db.entity.CPPR2TLDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimeLineDbo;
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
		if(input.getCompiledPeriodId() == null){
		 	errors.addFieldError(CPPR2TL.CPPR_ID_FIELD_NAME, CPPR2TLValidationErrors.CPPR_INVALID.getCode());
		} else {
			CompiledPeriodDbo cppr = cpprDao.read(input.getCompiledPeriodId());
			if(cppr == null){
				errors.addFieldError(CPPR2TL.CPPR_ID_FIELD_NAME, CPPR2TLValidationErrors.CPPR_INVALID.getCode());
			} 
		}
	}
	
	protected void validateTimeline(CPPR2TL input, Operation op, Errors errors) {
		if(input.getTimelineId() == null){
		 	errors.addFieldError(CPPR2TL.TL_ID_FIELD_NAME, CPPR2TLValidationErrors.TIMELINE_INVALID.getCode());
		} else {
			TimeLineDbo timeline = tlDao.read(input.getTimelineId());
			if(timeline == null){
				errors.addFieldError(CPPR2TL.TL_ID_FIELD_NAME, CPPR2TLValidationErrors.TIMELINE_INVALID.getCode());
			} 
		}
	}

	@Override
	protected CPPR2TLDao getDao() {
		return dao;
	}
	
}
