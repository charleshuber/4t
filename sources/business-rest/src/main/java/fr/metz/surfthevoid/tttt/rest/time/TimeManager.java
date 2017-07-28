package fr.metz.surfthevoid.tttt.rest.time;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Named;

import org.quartz.CronExpression;
import org.springframework.scheduling.support.CronSequenceGenerator;

@Named
public class TimeManager {
	
	
	protected List<TimeInterval> getTimeIntervals(TimeLine timeline) throws ParseException{
		List<TimeInterval> results = new ArrayList<>();
		
		CronExpression cronExpression = new CronExpression(timeline.getCronExpression());
		cronExpression.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		if(CronSequenceGenerator.isValidExpression(timeline.getCronExpression())){
			// TODO fill default start and end date
			CronSequenceGenerator generator = new CronSequenceGenerator(timeline.getCronExpression());
			
			Date inDate = Date.from(timeline.getStartTime().toInstant(ZoneOffset.UTC));
			Date outDate = Date.from(timeline.getStartTime().toInstant(ZoneOffset.UTC));
			Date startDate = new Date(0);
			
			//TODO Attention au période en cours à la inDate. => Il faut choper l'évènement précedent..challenge
			/*
			do{
				generator.
			} while(inDate.compareTo(outDate) <= 0);
			*/
		} else {
			// TODO Exception
		}
		
		return results;
	}
	
	
}
