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

import javax.inject.Inject;
import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimelineDao;
import fr.metz.surfthevoid.tttt.rest.resources.cronperiod.CronPeriod;
import fr.metz.surfthevoid.tttt.rest.resources.cronperiod.CronPeriodStore;
import fr.metz.surfthevoid.tttt.rest.resources.time.TimeInterval;
import fr.metz.surfthevoid.tttt.rest.time.cron.CronExpressionAnalyser;

@Named
public class TimeManager {
	
	public DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	public ZoneOffset zoneOffset = ZoneOffset.UTC;
	
	@Inject
	protected TimelineDao timelineDao;
	@Inject
	protected CompiledPeriodDao cpprDao;
	@Inject
	protected CronPeriodStore cronPeriodStore;
	
	public Set<TimeInterval> timelineCompilation(Long tlid, Date start, Date end) {
		
		return null;
	}
	
	public Set<TimeInterval> cpprCompilation(Long tlid, Date start, Date end) {
		// TODO Auto-generated method stub
		return null;
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
	
	protected List<TimeInterval> optimizeIntervals(List<TimeInterval> intervals){
		LinkedList<TimeInterval> source = new LinkedList<TimeInterval>(intervals);
		LinkedList<TimeInterval> dest = new LinkedList<TimeInterval>();
		source.sort(Comparator.comparing(TimeInterval::getStartTime));
		
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
		return dest;
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
