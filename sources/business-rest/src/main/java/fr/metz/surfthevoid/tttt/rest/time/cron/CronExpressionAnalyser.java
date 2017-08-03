package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.quartz.CronExpression;

import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInMonthParser.DaysInMonthParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInWeekParser.DaysInWeekParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.HoursParser.HoursParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.MonthsParser.MonthsParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.SecondsMinutesParser.SecondsMinutesParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.YearsParser.YearsParsingResult;

public class CronExpressionAnalyser {
	
	protected String expression;
	protected SecondsMinutesParsingResult seconds;
    protected SecondsMinutesParsingResult minutes;
    protected HoursParsingResult hours;
    protected DaysInMonthParsingResult daysOfMonth;
    protected MonthsParsingResult months;
    protected DaysInWeekParsingResult daysOfWeek;
    protected YearsParsingResult years;
	
	public static void main(String...strings) throws ParseException{
		/*
		a.parse("0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * *");
		
		a.parse("* * * LW * *");
		*/
		
		List<String> expressions = Arrays.asList(
		"0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * ? 2001",
		"5-27 10/5 23 ? * L-2 2001,2003,2440-2444,2100/3,2012/6",
		"10/5 5-27 12,21,2-4,3/5 ? * MONL 2440-2444",
		"0,1,40,5-7,5/6 10/5,7,15/3 10/5 ? * MON#3 2100/3",
		"0 10 4 ? * 7 2020");
		
		for(String expression : expressions){

			Long t1 = new Date().getTime();
			//CronExpressionAnalyser exp = new CronExpressionAnalyser(expression);
			Long t2 = new Date().getTime();
			//System.out.println("toi:" + (t2 - t1) + "; exp:" + exp);
			
			//exp.next(LocalDateTime.now());

			
			t1 = new Date().getTime();
			CronExpression exp2 = new CronExpression(expression);
			t2 = new Date().getTime();
			System.out.println("cron:" + (t2 - t1) + "; exp:" + exp2);
			System.out.println("");
			System.out.println("");
			System.out.println(exp2.getTimeAfter(new Date()));
		}
		
	}
	
	public LocalDateTime next(LocalDateTime current){
		
		DateValidity validity = new DateValidity(current);
		
		if(validity.isCurrentMinuteValid){
			LocalDateTime nextSecond = seconds.rollToNext(current, ChronoField.SECOND_OF_MINUTE);
			// Next date can be before current date due to rolling next implementation. Minute cannot be incremented
			if(nextSecond.isAfter(current)){
				//A next valid second value is available for the current valid minute
				return nextSecond;
			}
		} 
		
		if(validity.isCurrentHourValid){
			LocalDateTime nextSecond = seconds.rollToNext(current.withSecond(0), ChronoField.SECOND_OF_MINUTE);
			LocalDateTime nextMinute = minutes.rollToNext(nextSecond, ChronoField.MINUTE_OF_HOUR);
			// Next date can be before current date, due to rolling next implementation. Hour cannot be incremented
			if(nextMinute.isAfter(current)) {
				//A next valid minute value is available for the current valid hour
				return nextMinute;
			}
		}
		
		if(validity.isCurrentDayValid){
			LocalDateTime nextSecond = seconds.rollToNext(current.withSecond(0), ChronoField.SECOND_OF_MINUTE);
			LocalDateTime nextMinute = minutes.rollToNext(nextSecond.withMinute(0), ChronoField.MINUTE_OF_HOUR);
			LocalDateTime nextHour = hours.rollToNext(nextMinute, ChronoField.HOUR_OF_DAY);
			// Next date can be before current date due to rolling next implementation. Day cannot be incremented
			if(nextHour.isAfter(current)) {
				//A next valid hour value is available for the current valid day
				return nextHour;
			}
		}
		
		if(validity.isCurrentMonthValid){
			LocalDateTime nextSecond = seconds.rollToNext(current.withSecond(0), ChronoField.SECOND_OF_MINUTE);
			LocalDateTime nextMinute = minutes.rollToNext(nextSecond.withMinute(0), ChronoField.MINUTE_OF_HOUR);
			LocalDateTime nextHour = hours.rollToNext(nextMinute.withHour(0), ChronoField.HOUR_OF_DAY);
			LocalDateTime nextDay = null;
			if(daysOfMonth.unknown){
				nextDay = daysOfWeek.rollToNext(nextHour, ChronoField.DAY_OF_WEEK);
			} else {
				nextDay = daysOfMonth.rollToNext(nextHour, ChronoField.DAY_OF_MONTH);
			}
			//Warn a day of month can be null if its definition does not match any day of the current month
			if(nextDay != null && nextDay.isAfter(current)) 
				return nextDay;
		}
		
		LocalDateTime nextYear = years.rollToNext(current, ChronoField.YEAR);
		
		return null;
	}
	
	private class DateValidity{
		private Boolean isCurrentYearValid = false;
		private Boolean isCurrentMonthValid = false;
		private Boolean isCurrentDayValid = false;
		private Boolean isCurrentHourValid = false;
		private Boolean isCurrentMinuteValid = false;
		
		private DateValidity(LocalDateTime current){
			this.isCurrentYearValid = years.isValid(current, ChronoField.YEAR);
			if(years.isValid(current, ChronoField.YEAR)){
				this.isCurrentYearValid = true;
				if(months.isValid(current, ChronoField.MONTH_OF_YEAR)){
					this.isCurrentMonthValid = true;
					if(daysOfMonth.isValid(current, ChronoField.DAY_OF_MONTH) 
							&& daysOfWeek.isValid(current, ChronoField.DAY_OF_WEEK)){
						this.isCurrentDayValid = true;
						if(hours.isValid(current, ChronoField.HOUR_OF_DAY)){
							this.isCurrentHourValid = true;
							if(minutes.isValid(current, ChronoField.MINUTE_OF_HOUR)){
								this.isCurrentMinuteValid = true;
							}
						}
					}
				}
			}
		}
	}
	
	public CronExpressionAnalyser(String cronExpression){
		String[] individuals = cronExpression.split("\\s+");
		if(individuals.length < 6 || individuals.length > 7){
			throw new IllegalArgumentException();
		}
		SecondsMinutesParser smParser = new SecondsMinutesParser();
		seconds = smParser.parse(individuals[0]);
		minutes = smParser.parse(individuals[1]);
		hours = new HoursParser().parse(individuals[2]);
		daysOfMonth = new DaysInMonthParser().parse(individuals[3]);
		months = new MonthsParser().parse(individuals[4]);
		daysOfWeek = new DaysInWeekParser().parse(individuals[5]);
		
		if(individuals.length == 7){
			years = new YearsParser().parse(individuals[6]);
		}
		
		//TODO validate exclusivity of day policy
		
		this.expression = cronExpression;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CronExpressionAnalyser [expression=");
		builder.append(expression);
		builder.append(", seconds=");
		builder.append(seconds);
		builder.append(", minutes=");
		builder.append(minutes);
		builder.append(", hours=");
		builder.append(hours);
		builder.append(", daysOfMonth=");
		builder.append(daysOfMonth);
		builder.append(", months=");
		builder.append(months);
		builder.append(", daysOfWeek=");
		builder.append(daysOfWeek);
		builder.append(", years=");
		builder.append(years);
		builder.append("]");
		return builder.toString();
	}		
}
