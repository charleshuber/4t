package fr.metz.surfthevoid.tttt.rest.resources.timeline;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CronPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.PeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.CronPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.PeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimelineDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;

@Named
public class TimelineStore extends ResourceStore<Timeline, TimelineDbo>{
	
	@Inject
	protected TimelineDao dao;
	@Inject
	protected CompiledPeriodDao cpprDao;
	@Inject
	protected CronPeriodDao crprDao;
	@Inject
	protected PeriodDao periodDao;
	
	@Inject
	protected TimelineValidator validator;
	
	public Set<Timeline> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toCollection(getOrderedById()));
	}
	
	public void addCPPR(Long timelineId, Long cpprId) throws ValidationException {
		validator.validateCPPRAddition(timelineId, cpprId);
		TimelineDbo dbTimeline = dao.read(timelineId);
		CompiledPeriodDbo dbCPPR = cpprDao.read(cpprId);
		if(dbTimeline.getCompPeriods() == null){
			dbTimeline.setCompPeriods(new HashSet<CompiledPeriodDbo>());
		}
		dbTimeline.getCompPeriods().add(dbCPPR);
	}

	public void removeCPPR(Long timelineId, Long cpprId) throws ValidationException {
		validator.validateCPPRDeletion(timelineId, cpprId);
		TimelineDbo dbTimeline = dao.read(timelineId);
		CompiledPeriodDbo dbCPPR = cpprDao.read(cpprId);
		dbTimeline.getCompPeriods().remove(dbCPPR);
	}
	
	public void addCRPR(Long timelineId, Long crprId) throws ValidationException {
		validator.validateCRPRAddition(timelineId, crprId);
		TimelineDbo dbTimeline = dao.read(timelineId);
		CronPeriodDbo dbCRPR = crprDao.read(crprId);
		if(dbTimeline.getCronPeriods() == null) dbTimeline.setCronPeriods(new HashSet<CronPeriodDbo>());
		dbTimeline.getCronPeriods().add(dbCRPR);
	}
	
	public void removeCRPR(Long timelineId, Long crprId) throws ValidationException {
		validator.validateCRPRDeletion(timelineId, crprId);
		TimelineDbo dbTimeline = dao.read(timelineId);
		CronPeriodDbo dbCRPR = crprDao.read(crprId);
		dbTimeline.getCronPeriods().remove(dbCRPR);
	}
	
	public void addPeriod(Long timelineId, Long periodId) throws ValidationException {
		validator.validatePeriodAddition(timelineId, periodId);
		TimelineDbo dbTimeline = dao.read(timelineId);
		PeriodDbo dbPeriod = periodDao.read(periodId);
		if(dbTimeline.getPeriods() == null) dbTimeline.setPeriods(new HashSet<PeriodDbo>());
		dbTimeline.getPeriods().add(dbPeriod);
	}
	
	public void removePeriod(Long timelineId, Long periodId) throws ValidationException {
		validator.validatePeriodDeletion(timelineId, periodId);
		TimelineDbo dbTimeline = dao.read(timelineId);
		PeriodDbo dbPeriod = periodDao.read(periodId);
		dbTimeline.getPeriods().remove(dbPeriod);
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
