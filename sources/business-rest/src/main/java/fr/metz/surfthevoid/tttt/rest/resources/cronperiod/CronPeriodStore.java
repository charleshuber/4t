package fr.metz.surfthevoid.tttt.rest.resources.cronperiod;

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

import fr.metz.surfthevoid.tttt.rest.db.entity.CronPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CronPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimelineDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

@Named
public class CronPeriodStore extends ResourceStore<CronPeriod, CronPeriodDbo>{
	
	@Inject
	protected CronPeriodDao dao;
	
	@Inject
	protected TimelineDao tlDao;
	
	@Inject
	protected CronPeriodValidator validator;
	
	public Set<CronPeriod> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toCollection(getOrderedById()));
	}
	
	public Set<CronPeriod> allOfTimeline(Long tlid) throws ValidationException {
		TimelineDbo dbTimeLine = tlDao.read(tlid);			
		if(dbTimeLine != null){
			if(CollectionUtils.isNotEmpty(dbTimeLine.getCompPeriods())){
				return dbTimeLine.getCronPeriods().stream()
				.map(dbCRPR -> extract(dbCRPR))
				.collect(Collectors.toCollection(getOrderedById()));
			}
			return new HashSet<CronPeriod>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	@Override
	protected CronPeriodDao getDao() {
		return dao;
	}

	@Override
	protected CronPeriodValidator getValidator() {
		return validator;
	}

	@Override
	protected CronPeriodDbo transform(CronPeriod res) {
		CronPeriodDbo cronPeriodDbo = new CronPeriodDbo();
		cronPeriodDbo.setId(res.getId());
		cronPeriodDbo.setScExp(res.getExpSeconds());
		cronPeriodDbo.setMnExp(res.getExpMinutes());
		cronPeriodDbo.setHrExp(res.getExpHours());
		cronPeriodDbo.setDmExp(res.getExpDaysOfMonths());
		cronPeriodDbo.setMtExp(res.getExpMonths());
		cronPeriodDbo.setDwExp(res.getExpDaysOfWeeks());
		cronPeriodDbo.setYrExp(res.getExpYears());
		cronPeriodDbo.setScDur(res.getSeconds());
		cronPeriodDbo.setMnDur(res.getMinutes());
		cronPeriodDbo.setHrDur(res.getHours());
		cronPeriodDbo.setDyDur(res.getDays());
		cronPeriodDbo.setMtDur(res.getMonths());
		cronPeriodDbo.setYrDur(res.getYears());
		return cronPeriodDbo;
	}

	@Override
	public CronPeriod extract(CronPeriodDbo dbo) {
		CronPeriod cronPeriod = new CronPeriod();
		cronPeriod.setId(dbo.getId());
		cronPeriod.setExpSeconds(dbo.getScExp());
		cronPeriod.setExpMinutes(dbo.getMnExp());
		cronPeriod.setExpHours(dbo.getHrExp());
		cronPeriod.setExpDaysOfMonths(dbo.getDmExp());
		cronPeriod.setExpMonths(dbo.getMtExp());
		cronPeriod.setExpDaysOfWeeks(dbo.getDwExp());
		cronPeriod.setExpYears(dbo.getYrExp());
		cronPeriod.setSeconds(dbo.getScDur());
		cronPeriod.setMinutes(dbo.getMnDur());
		cronPeriod.setHours(dbo.getHrDur());
		cronPeriod.setDays(dbo.getDyDur());
		cronPeriod.setMonths(dbo.getMtDur());
		cronPeriod.setYears(dbo.getYrDur());
		return cronPeriod;
	}

	@Override
	protected CronPeriod clean(CronPeriod res, Operation op) {
		if(StringUtils.isNotEmpty(res.getExpSeconds())){
			res.setExpSeconds(res.getExpSeconds().trim());
		}
		if(StringUtils.isNotEmpty(res.getExpMinutes())){
			res.setExpMinutes(res.getExpMinutes().trim());
		}
		if(StringUtils.isNotEmpty(res.getExpHours())){
			res.setExpHours(res.getExpHours().trim());
		}
		if(StringUtils.isNotEmpty(res.getExpDaysOfMonths())){
			res.setExpDaysOfMonths(res.getExpDaysOfMonths().trim());
		}
		if(StringUtils.isNotEmpty(res.getExpMonths())){
			res.setExpMonths(res.getExpMonths().trim());
			
		}
		if(StringUtils.isNotEmpty(res.getExpDaysOfWeeks())){
			res.setExpDaysOfWeeks(res.getExpDaysOfWeeks().trim());
		}
		if(StringUtils.isNotEmpty(res.getExpYears())){
			res.setExpYears(res.getExpYears().trim());
			
		}
		return res;
	}
	
	public Supplier<TreeSet<CronPeriod>> getOrderedById(){
		return () -> new TreeSet<CronPeriod>(Comparator.comparing(CronPeriod::getId));
	}
}
