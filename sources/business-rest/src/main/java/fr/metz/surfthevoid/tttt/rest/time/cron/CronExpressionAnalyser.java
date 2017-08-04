package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    
    static String datePattern = "dd/MM/yyyy hh:mm:ss";
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
    static SimpleDateFormat df = new SimpleDateFormat(datePattern);
	
	public static void main(String...strings) throws ParseException{
		/*
		a.parse("0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * *");
		
		a.parse("* * * LW * *");
		*/
		
		List<String> expressions = Arrays.asList(
		"0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * ? 2020",
		"5-27 10/5 23 ? * 5/2 2001,2003,2440-2444,2100/3,2012/5",
		"10/5 5-27 12,21,2-4,3/5 ? * 2L 2440-2444",
		"0,1,40,5-7,5/6 10/5,7,15/3 10/5 ? * MON#3 2100/3",
		"0 10 4 ? * 7 2020");
		
		for(String expression : expressions){

			Long t1 = new Date().getTime();
			CronExpressionAnalyser exp = new CronExpressionAnalyser(expression);
			Long t2 = new Date().getTime();
			System.out.println("toi:" + (t2 - t1) + "; exp:" + exp);
			
			t1 = new Date().getTime();
			CronExpression exp2 = new CronExpression(expression);
			t2 = new Date().getTime();
			System.out.println("cron:" + (t2 - t1) + "; exp:" + exp2);
			System.out.println("");
			
			
			Optional<LocalDateTime> dateTime = exp.next(LocalDateTime.now());
			if(dateTime.isPresent())
				System.out.println("toi next:" + dateTime.get().format(dtf));
			else System.out.println("toi next:" + "null");
			
			Date date = exp2.getTimeAfter(new Date());
			if(date != null)
				System.out.println("cro next:" + df.format(date));
			else System.out.println("cro next:" + "null");

			System.out.println("");
			System.out.println("");
		}
		
	}
	
	public Optional<LocalDateTime> next(LocalDateTime current){
		try{
			return Optional.of(nextDate(current, null));
		} catch(NoMoreEventException e){
			return Optional.empty();
		}
	}
	
	protected LocalDateTime nextDate(LocalDateTime current, DateValidity validity) throws NoMoreEventException{
		validity = validity == null ? new DateValidity(current) : validity;
		if(validity.isCurrentMinuteValid){
			LocalDateTime nextSecond = seconds.rollToNext(current, ChronoField.SECOND_OF_MINUTE);
			// Next date can be before current date due to rolling next implementation. Minute cannot be incremented
			if(nextSecond.isAfter(current)){
				//A next valid second value is available for the current valid minute
				return nextSecond;
			}
		} 
		if(validity.isCurrentHourValid){
			LocalDateTime secondsReset = current.withSecond(0);
			LocalDateTime nextMinute = minutes.rollToNext(secondsReset, ChronoField.MINUTE_OF_HOUR);
			// Next date can be before current date, due to rolling next implementation. Hour cannot be incremented
			if(nextMinute.isAfter(current)) {
				//A next valid minute value is available for the current valid hour
				DateValidity nextMinuteValidity = new DateValidity(nextMinute);
				if(nextMinuteValidity.isCurrentSecondValid){
					return nextMinute;
				} else {
					return nextDate(nextMinute, nextMinuteValidity);
				}
			}
		}
		if(validity.isCurrentDayValid){
			LocalDateTime minutesReset = current.truncatedTo(ChronoUnit.MINUTES);
			LocalDateTime nextHour = hours.rollToNext(minutesReset, ChronoField.HOUR_OF_DAY);
			// Next date can be before current date due to rolling next implementation. Day cannot be incremented
			if(nextHour.isAfter(current)) {
				//A next valid hour value is available for the current valid hour
				DateValidity nextHourValidity = new DateValidity(nextHour);
				if(nextHourValidity.isCurrentSecondValid){
					return nextHour;
				} else {
					return nextDate(nextHour, nextHourValidity);
				}
			}
		}
		if(validity.isCurrentMonthValid){
			LocalDateTime hourReset = current.truncatedTo(ChronoUnit.HOURS);
			LocalDateTime nextDay = null;
			if(daysOfMonth.unknown){
				nextDay = daysOfWeek.rollToNext(hourReset, ChronoField.DAY_OF_WEEK);
			} else {
				nextDay = daysOfMonth.rollToNext(hourReset, ChronoField.DAY_OF_MONTH);
			}
			//Warn a day of month can be null if its definition does not match any day of the current month
			if(nextDay != null && nextDay.isAfter(current)) {
				DateValidity nextDayValidity = new DateValidity(nextDay);
				if(nextDayValidity.isCurrentSecondValid){
					return nextDay;
				} else {
					return nextDate(nextDay, nextDayValidity);
				}
			}
		}		
		if(validity.isCurrentYearValid){
			LocalDateTime monthReset = current.withMonth(1).truncatedTo(ChronoUnit.DAYS);
			LocalDateTime nextMonth = months.rollToNext(monthReset, ChronoField.HOUR_OF_DAY);
			// Next date can be before current date due to rolling next implementation. Day cannot be incremented
			if(nextMonth.isAfter(current)) {
				//A next valid month value is available for the current valid hour
				DateValidity nextMonthValidity = new DateValidity(nextMonth);
				if(nextMonthValidity.isCurrentSecondValid){
					return nextMonth;
				} else {
					return nextDate(nextMonth, nextMonthValidity);
				}
			}
		}
		
		// at this step there is no more event for the current year, one step forward
		LocalDateTime nextYear = years.rollToNext(current, ChronoField.YEAR);
 		if(nextYear == null){
			throw new NoMoreEventException();
		}
		
 		//reset the nextYear
 		nextYear = nextYear.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1).withMonth(1);
		DateValidity nextDateValidity = new DateValidity(nextYear);
		
 		if(nextDateValidity.isCurrentSecondValid)
 			return nextYear;
		
		return nextDate(nextYear, nextDateValidity);
	}
	
	private class DateValidity{
		private Boolean isCurrentYearValid = false;
		private Boolean isCurrentMonthValid = false;
		private Boolean isCurrentDayValid = false;
		private Boolean isCurrentHourValid = false;
		private Boolean isCurrentMinuteValid = false;
		private Boolean isCurrentSecondValid = false;
		
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
								if(seconds.isValid(current, ChronoField.SECOND_OF_MINUTE))
									this.isCurrentSecondValid = true;
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
	
	public class NoMoreEventException extends Exception {
		private static final long serialVersionUID = 4179478162562343723L;
	
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
