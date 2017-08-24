package fr.metz.surfthevoid.tttt.rest.resources.period;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.PeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimeLineDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.PeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimeLineDao;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

@Named
public class PeriodStore extends ResourceStore<Period, PeriodDbo>{
	
	@Inject
	protected PeriodDao dao;
	
	@Inject
	protected TimeLineDao tlDao;
	
	@Inject
	protected PeriodValidator validator;
	
	public Set<Period> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toCollection(getOrderedById()));
	}
	
	public Set<Period> allOfTimeline(Long tlid) throws ValidationException {
		TimeLineDbo dbTimeLine = tlDao.read(tlid);			
		if(dbTimeLine != null){
			if(CollectionUtils.isNotEmpty(dbTimeLine.getCompPeriods())){
				return dbTimeLine.getPeriods().stream()
				.map(dbCRPR -> extract(dbCRPR))
				.collect(Collectors.toCollection(getOrderedById()));
			}
			return new HashSet<Period>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	@Override
	protected PeriodDao getDao() {
		return dao;
	}

	@Override
	protected PeriodValidator getValidator() {
		return validator;
	}

	@Override
	protected PeriodDbo transform(Period res) {
		PeriodDbo cronPeriodDbo = new PeriodDbo();
		cronPeriodDbo.setId(res.getId());
		cronPeriodDbo.setStartTime(res.getStartTime());
		cronPeriodDbo.setEndTime(res.getEndTime());
		return cronPeriodDbo;
	}

	@Override
	public Period extract(PeriodDbo dbo) {
		Period cronPeriod = new Period();
		cronPeriod.setId(dbo.getId());
		cronPeriod.setStartTime(dbo.getStartTime());
		cronPeriod.setEndTime(dbo.getEndTime());
		return cronPeriod;
	}
	
	public Supplier<TreeSet<Period>> getOrderedById(){
		return () -> new TreeSet<Period>(Comparator.comparing(Period::getId));
	}
}
