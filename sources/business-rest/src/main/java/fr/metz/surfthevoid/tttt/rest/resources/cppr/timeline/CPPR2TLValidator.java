package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CPPR2TLDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CPPR2TLDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimelineDao;
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
	protected TimelineDao tlDao;
	
	@Override
	public void validateInput(CPPR2TL input, Operation op, Errors errors) throws ValidationException {
		validateCPPR(input, op, errors);
		validateTimeline(input, op, errors);
		validateCyclicDependency(input, op, errors);
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
			TimelineDbo timeline = tlDao.read(input.getTimelineId());
			if(timeline == null){
				errors.addFieldError(CPPR2TL.TL_ID_FIELD_NAME, CPPR2TLValidationErrors.TIMELINE_INVALID.getCode());
			} 
		}
	}

	protected void validateCyclicDependency(CPPR2TL input, Operation op, Errors errors) {
		CompiledPeriodDbo dbCPPR = cpprDao.read(input.getCompiledPeriodId());
		TimelineDbo dbTimeline = tlDao.read(input.getTimelineId());
		if(dbTimeline != null 
				&& dbCPPR != null 
				&& isTimelineContainingCPPR(dbTimeline, dbCPPR)) {
			errors.addGlobalError(CPPR2TLValidationErrors.CYCLIC_DEPENDENCY.getCode());
		}
	}
	
	public boolean isTimelineContainingCPPR(TimelineDbo dbTimeline, CompiledPeriodDbo dbCPPR) {
		if(CollectionUtils.isEmpty(dbTimeline.getCompPeriods())){
			return false;
		}
		Optional<CompiledPeriodDbo> optDirectLink = dbTimeline.getCompPeriods().stream()
				.filter(cppr -> cppr == dbCPPR)
				.findFirst();
		
		if(optDirectLink.isPresent()){
			return true;
		}
		return dbTimeline.getCompPeriods().stream()
				.filter(cppr -> {
					if(CollectionUtils.isNotEmpty(cppr.getCp2tls())){
						for(CPPR2TLDbo link : cppr.getCp2tls()){
							if(isTimelineContainingCPPR(link.getTimeline(), dbCPPR)){
								return true;
							}
						}
					}
				 return false;
				}).findFirst().isPresent();
	}
	
	@Override
	protected CPPR2TLDao getDao() {
		return dao;
	}
	
}
