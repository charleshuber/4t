package fr.metz.surfthevoid.tttt.rest.resources.timeline;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimelineDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

@Named
public class TimelineStore extends ResourceStore<Timeline, TimelineDbo>{
	
	@Inject
	protected TimelineDao dao;
	@Inject
	protected CompiledPeriodDao cpprDao;
	
	@Inject
	protected TimelineDao tlDao;
	
	@Inject
	protected TimelineValidator validator;
	
	public Set<Timeline> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toCollection(getOrderedById()));
	}
	
	public Boolean addCPPR(Long timelineId, Long cpprId) throws ValidationException {
		TimelineDbo dbTimeline = dao.read(timelineId);
		CompiledPeriodDbo dbCPPR = cpprDao.read(cpprId);
		
		TODO ADD CYCLIC VALIDATION
		
		if(dbTimeline != null && dbCPPR != null){
			if(CollectionUtils.isNotEmpty(dbTimeline.getCompPeriods()) 
					&& dbTimeline.getCompPeriods().contains(dbCPPR)){
				Errors errors = new Errors();
				errors.addGlobalError("The compiled period with id " + cpprId + " is already a element of the timeline with id " + timelineId);
				throw new ValidationException(Type.CONFLICT, errors);
			}
			if(dbTimeline.getCompPeriods() == null) dbTimeline.setCompPeriods(new HashSet<CompiledPeriodDbo>());
			dbTimeline.getCompPeriods().add(dbCPPR);
			return true;
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	public Boolean removeCPPR(Long timelineId, Long cpprId) throws ValidationException {
		TimelineDbo dbTimeline = dao.read(timelineId);
		CompiledPeriodDbo dbUser = cpprDao.read(cpprId);
		if(dbTimeline != null && dbUser != null){
			if(CollectionUtils.isNotEmpty(dbTimeline.getUsers()) 
					&& dbTimeline.getUsers().contains(dbUser)){
				dbTimeline.getUsers().remove(dbUser);
				return true;
			}
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	@Override
	protected TimelineDao getDao() {
		return dao;
	}

	@Override
	protected TimelineValidator getValidator() {
		return validator;
	}

	@Override
	protected TimelineDbo transform(Timeline res) {
		TimelineDbo cronPeriodDbo = new TimelineDbo();
		cronPeriodDbo.setId(res.getId());
		cronPeriodDbo.setName(res.getName());
		return cronPeriodDbo;
	}

	@Override
	public Timeline extract(TimelineDbo dbo) {
		Timeline cronPeriod = new Timeline();
		cronPeriod.setId(dbo.getId());
		cronPeriod.setName(dbo.getName());
		return cronPeriod;
	}

	@Override
	protected Timeline clean(Timeline res, Operation op) {
		if(StringUtils.isNotEmpty(res.getName())){
			res.setName(res.getName().trim());
		}
		return res;
	}
	
	public Supplier<TreeSet<Timeline>> getOrderedById(){
		return () -> new TreeSet<Timeline>(Comparator.comparing(Timeline::getId));
	}
}
