package fr.metz.surfthevoid.tttt.rest.resources.timeline;


import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CPPR2TLDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CronPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.CronPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.PeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimelineDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

@Named
public class TimelineValidator extends Validator<Timeline, TimelineDbo>{
	
	@Inject
	protected TimelineDao dao;
	@Inject
	protected CompiledPeriodDao cpprDao;
	@Inject
	protected CronPeriodDao crprDao;
	@Inject
	protected PeriodDao periodDao;
	
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
	
	public void validateCPPRAddition(Long timelineId, Long cpprId) throws ValidationException{
		TimelineDbo dbTimeline = dao.read(timelineId);
		CompiledPeriodDbo dbCPPR = cpprDao.read(cpprId);
		if(dbTimeline == null || dbCPPR == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		} 
		else if(CollectionUtils.isNotEmpty(dbTimeline.getCompPeriods()) 
					&& dbTimeline.getCompPeriods().contains(dbCPPR)){
			throw new ValidationException(Type.CONFLICT, null);
			
		}
		//Avoid cyclic dependency
		else if(isCPPRContainingTimeline(dbCPPR, dbTimeline)){
			Errors errors = new Errors();
			errors.addGlobalError(TimelineValidationErrors.CYCLIC_DEPENDENCY.getCode());
			throw new ValidationException(Type.INVALID_INPUT, errors);
		}
	}
	
	public void validateCPPRDeletion(Long timelineId, Long crprId) throws ValidationException{
		TimelineDbo dbTimeline = dao.read(timelineId);
		CompiledPeriodDbo dbCPPR = cpprDao.read(crprId);
		if(dbTimeline == null || dbCPPR == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		}
	}
	
	
	public void validateCRPRAddition(Long timelineId, Long crprId) throws ValidationException{
		TimelineDbo dbTimeline = dao.read(timelineId);
		CronPeriodDbo dbCRPR = crprDao.read(crprId);
		if(dbTimeline == null || dbCRPR == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		} 
		else if(CollectionUtils.isNotEmpty(dbTimeline.getCompPeriods()) 
					&& dbTimeline.getCompPeriods().contains(dbCRPR)){
			throw new ValidationException(Type.CONFLICT, null);
			
		}
	}
	
	public void validateCRPRDeletion(Long timelineId, Long crprId) throws ValidationException{
		TimelineDbo dbTimeline = dao.read(timelineId);
		CronPeriodDbo dbCRPR = crprDao.read(crprId);
		if(dbTimeline == null || dbCRPR == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		}
	}
	
	
	protected boolean isCPPRContainingTimeline(CompiledPeriodDbo dbCPPR, TimelineDbo dbTimeline) {
		if(CollectionUtils.isEmpty(dbCPPR.getCp2tls())){
			return false;
		}
		Optional<CPPR2TLDbo> optDirectLink = dbCPPR.getCp2tls().stream()
		.filter(link -> link.getTimeline() == dbTimeline)
		.findFirst();
		
		if(optDirectLink.isPresent()){
			return true;
		}
		return dbCPPR.getCp2tls().stream()
				.filter(link -> isCPPRContainingTimeline(dbCPPR, link.getTimeline()))
				.findFirst().isPresent();
	}

	@Override
	protected TimelineDao getDao() {
		return dao;
	}
	
}
