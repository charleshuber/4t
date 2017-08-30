package fr.metz.surfthevoid.tttt.rest.time;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.metz.surfthevoid.tttt.rest.db.entity.CPPR2TLDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CronPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.PeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimelineDao;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;
import fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline.CPPR2TLValidator;
import fr.metz.surfthevoid.tttt.rest.resources.cronperiod.CronPeriod;
import fr.metz.surfthevoid.tttt.rest.resources.cronperiod.CronPeriodStore;
import fr.metz.surfthevoid.tttt.rest.resources.period.Period;
import fr.metz.surfthevoid.tttt.rest.resources.period.PeriodStore;
import fr.metz.surfthevoid.tttt.rest.resources.time.TimeInterval;
import fr.metz.surfthevoid.tttt.rest.resources.timeline.TimelineValidator;
import fr.metz.surfthevoid.tttt.rest.time.cron.CronExpressionAnalyser;

@Named
@Transactional(TxType.REQUIRED)
public class TimeCompilator {
	
	protected Log log = LogFactory.getLog(getClass());
	protected DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	protected ZoneOffset zoneOffset = ZoneOffset.UTC;
	protected Comparator<TimeInterval> itvComparator = Comparator.comparing(TimeInterval::getStartTime).thenComparing(TimeInterval::getEndTime);
	
	@Inject
	protected TimelineDao timelineDao;
	@Inject
	protected CompiledPeriodDao cpprDao;
	@Inject
	protected PeriodStore periodStore;
	@Inject
	protected CronPeriodStore cronPeriodStore;
	@Inject
	protected TimelineValidator timelineValidator;
	@Inject
	protected CPPR2TLValidator cppr2TLValidator;
	
	public Set<TimeInterval> cpprCompilation(Long cpprid, Date start, Date end) throws ValidationException {
		try {
			return cpprCompilation(cpprid, 
					LocalDateTime.ofInstant(start.toInstant(), ZoneOffset.UTC), 
					LocalDateTime.ofInstant(end.toInstant(), ZoneOffset.UTC));
		} catch (ParseException e) {
			log.error("A cron expression could not be parsed", e);
			throw new ValidationException(Type.INTERNAL_ERROR, null);
		}
	}
	
	public Set<TimeInterval> timelineCompilation(Long tlid, Date start, Date end) throws ValidationException {
		try {
			return timelineCompilation(tlid, 
					LocalDateTime.ofInstant(start.toInstant(), ZoneOffset.UTC), 
					LocalDateTime.ofInstant(end.toInstant(), ZoneOffset.UTC));
		} catch (ParseException e) {
			log.error("A cron expression could not be parsed", e);
			throw new ValidationException(Type.INTERNAL_ERROR, null);
		}
	}
	
	protected Set<TimeInterval> cpprCompilation(Long cpprid, LocalDateTime start, LocalDateTime end) throws ValidationException, ParseException {
		List<TimeInterval> cpprIntervals = new ArrayList<>();
		
		CompiledPeriodDbo cppr = cpprDao.read(cpprid);
		if(cppr == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		}
		
		validateNoCyclicDependency(cppr);
		
		if(CollectionUtils.isNotEmpty(cppr.getCp2tls())){
			Set<CPPR2TLDbo> orderedLayers = new TreeSet<CPPR2TLDbo>(Comparator.comparing(CPPR2TLDbo::getOrder));
			orderedLayers.addAll(cppr.getCp2tls());
			for(CPPR2TLDbo cppr2TL : orderedLayers){
				Set<TimeInterval> timelineIntervals = timelineCompilation(cppr2TL.getTimeline().getId(), start, end);
				if(cppr2TL.getNegative()){
					cpprIntervals = substractTimeIntervals(cpprIntervals, timelineIntervals);
				} else {
					cpprIntervals.addAll(timelineIntervals);
				}
			}
		}
		
		return optimizeIntervals(cpprIntervals);
	}

	protected Set<TimeInterval> timelineCompilation(Long tlid, LocalDateTime start, LocalDateTime end) throws ValidationException, ParseException {	
		List<TimeInterval> allIntervals = new ArrayList<>();
		
		TimelineDbo timeline = timelineDao.read(tlid);
		if(timeline == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		}

		validateNoCyclicDependency(timeline);
		
		if(CollectionUtils.isNotEmpty(timeline.getPeriods())){
			for(PeriodDbo period : timeline.getPeriods()){
				allIntervals.add(toTimeInterval(periodStore.extract(period), start, end));
			}
		}
		
		if(CollectionUtils.isNotEmpty(timeline.getCronPeriods())){
			for(CronPeriodDbo crpr : timeline.getCronPeriods()){
				allIntervals.addAll(getTimeIntervals(cronPeriodStore.extract(crpr), start, end));
			}
		}
		
		if(CollectionUtils.isNotEmpty(timeline.getCompPeriods())){
			for(CompiledPeriodDbo cppr : timeline.getCompPeriods()){
				allIntervals.addAll(cpprCompilation(cppr.getId(), start, end));
			}
		}
		
		return optimizeIntervals(allIntervals);
	}
	
	protected void validateNoCyclicDependency(TimelineDbo timeline) throws ValidationException {
		if(CollectionUtils.isNotEmpty(timeline.getCompPeriods())){
			for(CompiledPeriodDbo period : timeline.getCompPeriods()){
				if(timelineValidator.isCPPRContainingTimeline(period, timeline)){
					log.error("Database data is invalid. "
							+ "There is a cyclic dependency between timeline (" + timeline.getId() + ") and compiled period (" + period.getId() + ")");
					throw new ValidationException(Type.INTERNAL_ERROR, null);
				}
			}
		}
	}
	
	protected void validateNoCyclicDependency(CompiledPeriodDbo cppr) throws ValidationException {
		if(CollectionUtils.isNotEmpty(cppr.getCp2tls())){
			for(CPPR2TLDbo period : cppr.getCp2tls()){
				if(cppr2TLValidator.isTimelineContainingCPPR(period.getTimeline(), cppr)){
					log.error("Database data is invalid. "
							+ "There is a cyclic dependency between compiled period (" + cppr.getId() + ") and timeline (" + period.getId() + ")");
					throw new ValidationException(Type.INTERNAL_ERROR, null);
				}
			}
		}
	}

	protected TimeInterval toTimeInterval(Period period, LocalDateTime startPoint, LocalDateTime endPoint){
		LocalDateTime startPeriodTime = LocalDateTime.ofInstant(period.getStartTime().toInstant(), ZoneOffset.UTC);
		LocalDateTime endPeriodTime = LocalDateTime.ofInstant(period.getEndTime().toInstant(), ZoneOffset.UTC);
		if(endPoint.isBefore(startPeriodTime) || endPeriodTime.isBefore(startPoint)){
			return null;
		}
		
		LocalDateTime startTime = startPeriodTime;
		if(startTime.isBefore(startPoint)){
			startTime = startPoint;
		}
		
		LocalDateTime endTime = endPeriodTime;
		if(endTime.isAfter(endPoint)){
			endTime = endPoint;
		}
		
		return new TimeInterval(startTime, endTime);
	}
	
	protected List<TimeInterval> getTimeIntervals(CronPeriod cronPeriod, LocalDateTime startPoint, LocalDateTime endPoint) throws ParseException{
		List<TimeInterval> results = new ArrayList<>();
		
		CronExpressionAnalyser analyser = new CronExpressionAnalyser(cronPeriod.toString());
		Optional<LocalDateTime> optPreviousEvent = analyser.previous(startPoint);
		
		if(optPreviousEvent.isPresent()){
			LocalDateTime previousEvent = optPreviousEvent.get();
			LocalDateTime previousPeriodEnd = cronPeriod.addTo(previousEvent);
			if(previousPeriodEnd != null && previousPeriodEnd.isAfter(startPoint)){
				results.add(new TimeInterval(startPoint, previousPeriodEnd));
			}
		}
		
		Optional<LocalDateTime> optNextEvent = analyser.next(startPoint);
		
		while(optNextEvent.isPresent() && optNextEvent.get().isBefore(endPoint)){
			LocalDateTime nextEvent = optNextEvent.get();
			LocalDateTime nextPeriodEnd = cronPeriod.addTo(nextEvent);
			if(nextPeriodEnd.isAfter(endPoint)){
				nextPeriodEnd = endPoint;
			} 
			results.add(new TimeInterval(nextEvent, nextPeriodEnd));
			optNextEvent = analyser.next(nextEvent);
		}
		
		return results;
	}
	
	protected TreeSet<TimeInterval> optimizeIntervals(List<TimeInterval> intervals){
		LinkedList<TimeInterval> source = new LinkedList<TimeInterval>(intervals);
		LinkedList<TimeInterval> dest = new LinkedList<TimeInterval>();
		source.sort(itvComparator);
		
		TimeInterval older = source.pollFirst();
		dest.offer(older);
		
		while(!source.isEmpty()){
			TimeInterval youngerDest = dest.getLast();
			TimeInterval olderSource = source.pollFirst();
			
			if(olderSource.getStartTime().after(youngerDest.getEndTime())){
				dest.offer(olderSource);
				continue;
			} else {
				TimeInterval merged = new TimeInterval(youngerDest.getStartTime(), olderSource.getEndTime());
				//replace the younger (most recent) interval by the merged one
				dest.pollLast();
				dest.offer(merged);
			}
		}
		TreeSet<TimeInterval> results = new TreeSet<>(itvComparator);
		results.addAll(dest);
		return results;
	}
	
	protected List<TimeInterval> substractTimeIntervals(List<TimeInterval> sourceIntervals,
			Set<TimeInterval> intervalsToSubstract) {
		
		TreeSet<TimeInterval> source = optimizeIntervals(sourceIntervals);
		for(TimeInterval itvToSubstract :  intervalsToSubstract){
			substractTimeInterval(source, itvToSubstract);
		}
		
		return new ArrayList<TimeInterval>(source);
	}
	
	protected void substractTimeInterval(Set<TimeInterval> optimizedIntervals,
			TimeInterval itvToSubstract) {

		TreeSet<TimeInterval> toDelete = new TreeSet<>(itvComparator);
		TreeSet<TimeInterval> toAdd = new TreeSet<>(itvComparator);
		
		Date subItvStart = itvToSubstract.getStartTime();
		Date subItvEnd = itvToSubstract.getEndTime();

		for(TimeInterval currentItv : optimizedIntervals){
			Date currentItvStart = currentItv.getStartTime();
			Date currentItvEnd = currentItv.getEndTime();
			//currentItv is inside itvToSubstract => - + + -
			if((currentItvStart.equals(subItvStart) || currentItvStart.after(subItvStart)) 
					&& (currentItvEnd.equals(subItvEnd) || currentItvEnd.before(subItvEnd))){
				toDelete.add(currentItv);
			}
			//itvToSubstract is inside currentItv  => + - - + 
			else if((subItvStart.equals(currentItvStart) || subItvStart.after(currentItvStart)) 
					&& (subItvEnd.equals(currentItvEnd) || subItvEnd.before(currentItvEnd))){
				toDelete.add(currentItv);
				Date exclusiveEndPoint = new Date(subItvStart.getTime() - 1);
				if(currentItvStart.before(exclusiveEndPoint)){
					toAdd.add(new TimeInterval(currentItvStart, exclusiveEndPoint));
				}
				Date exclusiveStartPoint = new Date(subItvEnd.getTime() + 1);
				if(currentItvEnd.after(exclusiveStartPoint)){
					toAdd.add(new TimeInterval(exclusiveStartPoint, currentItvEnd));
				}
			}
			//itvToSubstract start is inside currentItv  => + - + - 
			else if((subItvStart.equals(currentItvStart) || subItvStart.after(currentItvStart)) 
						&& subItvStart.equals(currentItvEnd) || subItvStart.before(currentItvEnd)){
				toDelete.add(currentItv);
				Date exclusiveEndPoint = new Date(subItvStart.getTime() - 1);
				if(currentItvStart.before(exclusiveEndPoint)){
					toAdd.add(new TimeInterval(currentItvStart, exclusiveEndPoint));
				}
			}
			//itvToSubstract end is inside currentItv  => - + - +
			else if((subItvEnd.equals(currentItvStart) || subItvEnd.after(currentItvStart)) 
						&& subItvEnd.equals(currentItvEnd) || subItvEnd.before(currentItvEnd)){
				toDelete.add(currentItv);
				Date exclusiveStartPoint = new Date(subItvEnd.getTime() + 1);
				if(currentItvEnd.after(exclusiveStartPoint)){
					toAdd.add(new TimeInterval(exclusiveStartPoint, currentItvEnd));
				}
			}
		}
		optimizedIntervals.removeAll(toDelete);
		optimizedIntervals.addAll(toAdd);
	}
	
	/*
	public static void main(String... args) throws ParseException{
		TimeDuration tp = new TimeDuration();
		tp.setHours(1);
		TimeLine tl = new TimeLine();
		CronDefinition cr = new CronDefinition();
		tl.setCronDefintions(Arrays.asList(cr));
		
		cr.setPeriod(tp);
		tl.setStartTime(LocalDateTime.parse("01/01/2010 00:00:00", df));
		tl.setEndTime(LocalDateTime.parse("01/01/2011 00:00:00", df));
		cr.setCronExpression("0 0 1 * * ? *");
		
		TimeManager tm = new TimeManager();
		List<TimeInterval> results = tm.getTimeIntervals(tl);
		
		for(TimeInterval ti : results) System.out.println(ti);
		System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		for(TimeInterval ti : tm.optimizeIntervals(results)) System.out.println(ti);
		System.out.println("---------------------------------------------");
		
		cr.setCronExpression("0 1 * * * ? *");
		results = tm.getTimeIntervals(tl);
		
		for(TimeInterval ti : results) System.out.println(ti);
		System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		for(TimeInterval ti : tm.optimizeIntervals(results)) System.out.println(ti);
		
		System.out.println("---------------------------------------------");
		
		cr.setCronExpression("1 0 12-13 ? 1,2,3 MON#3 2002/8,2019-2021");
		tl.setStartTime(LocalDateTime.parse("01/01/2000 00:00:00", df));
		tl.setEndTime(LocalDateTime.parse("01/01/2030 00:00:00", df));
		results = tm.getTimeIntervals(tl);
		
		for(TimeInterval ti : results) System.out.println(ti);
		System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		for(TimeInterval ti : tm.optimizeIntervals(results)) System.out.println(ti);
			
	}
	*/
	
}
