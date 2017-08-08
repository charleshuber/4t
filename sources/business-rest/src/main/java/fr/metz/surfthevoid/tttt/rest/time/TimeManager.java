package fr.metz.surfthevoid.tttt.rest.time;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.time.cron.CronExpressionAnalyser;

@Named
public class TimeManager {
	
	public static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	public static void main(String... args) throws ParseException{
		TimePeriod tp = new TimePeriod();
		tp.setHours(1);
		TimeLine tl = new TimeLine();
		
		tl.setPeriod(tp);
		tl.setStartTime(LocalDateTime.parse("01/01/2010 00:00:00", df));
		tl.setEndTime(LocalDateTime.parse("02/01/2010 00:00:00", df));
		tl.setCronExpression("0 0 1 * * ? *");
		
		TimeManager tm = new TimeManager();
		List<TimeInterval> results = tm.getTimeIntervals(tl);
		
		for(TimeInterval ti : results) System.out.println(ti);
		System.out.println("---------------------------------------------");
		
		tl.setCronExpression("0 1 * * * ? *");
		results = tm.getTimeIntervals(tl);
		
		for(TimeInterval ti : results) System.out.println(ti);
		
		System.out.println("---------------------------------------------");
		
		tl.setCronExpression("1 0 * * * ? *");
		results = tm.getTimeIntervals(tl);
		
		for(TimeInterval ti : results) System.out.println(ti);
		
		
		
	}
	
	protected List<TimeInterval> getTimeIntervals(TimeLine timeline) throws ParseException{
		List<TimeInterval> results = new ArrayList<>();
			
		LocalDateTime startPoint = timeline.getStartTime();
		LocalDateTime endPoint = timeline.getEndTime();
		
		CronExpressionAnalyser analyser = new CronExpressionAnalyser(timeline.getCronExpression());
		Optional<LocalDateTime> optPreviousEvent = analyser.previous(startPoint);
		
		if(optPreviousEvent.isPresent()){
			LocalDateTime previousEvent = optPreviousEvent.get();
			LocalDateTime previousPeriodEnd = timeline.getPeriod().addTo(previousEvent);
			if(previousPeriodEnd != null && previousPeriodEnd.isAfter(startPoint)){
				results.add(new TimeInterval(startPoint, previousPeriodEnd));
			}
		}
		
		Optional<LocalDateTime> optNextEvent = analyser.next(startPoint);
		
		while(optNextEvent.isPresent() && optNextEvent.get().isBefore(endPoint)){
			LocalDateTime nextEvent = optNextEvent.get();
			LocalDateTime nextPeriodEnd = timeline.getPeriod().addTo(nextEvent);
			if(nextPeriodEnd.isAfter(endPoint)){
				nextPeriodEnd = endPoint;
			} 
			results.add(new TimeInterval(nextEvent, nextPeriodEnd));
			optNextEvent = analyser.next(nextEvent);
		}
		
		return results;
	}
	
	
}
