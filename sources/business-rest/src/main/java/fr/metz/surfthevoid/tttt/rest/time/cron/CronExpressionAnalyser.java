package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.quartz.CronExpression;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.ParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInMonthParser.DaysInMonthParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInWeekParser.DaysInWeekParsingResult;

public class CronExpressionAnalyser {
	
	protected String expression;
	protected ParsingResult seconds;
    protected ParsingResult minutes;
    protected ParsingResult hours;
    protected DaysInMonthParsingResult daysOfMonth;
    protected ParsingResult months;
    protected DaysInWeekParsingResult daysOfWeek;
    
    protected transient TreeSet<Integer> years;
	
	public static void main(String...strings) throws ParseException{
		/*
		a.parse("0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * *");
		
		a.parse("* * * LW * *");
		*/
		
		List<String> expressions = Arrays.asList(
		"0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * ?",
		"5-27 10/5  23 ? * L-2",
		"10/5 5-27 12,21,2-4,3/5 ? * MONL",
		"0,1,40,5-7,5/6 10/5,7,15/3 10/5 ? * MON#3");
		
		for(String expression : expressions){

			Long t1 = new Date().getTime();
			CronExpressionAnalyser exp = new CronExpressionAnalyser(expression);
			Long t2 = new Date().getTime();
			System.out.println("toi:" + (t2 - t1) + "; exp:" + exp);
			
			t1 = new Date().getTime();
			CronExpression exp2 = new CronExpression(expression);
			t2 = new Date().getTime();
			System.out.println("cron:" + (t2 - t1) + "; exp:" + exp2);
		}
		
	}
	
	public CronExpressionAnalyser(String cronExpression){
		String[] individuals = cronExpression.split("\\s+");
		if(individuals.length < 6){
			throw new IllegalArgumentException();
		}
		SecondsMinutesParser smParser = new SecondsMinutesParser();
		HoursParser hParser = new HoursParser();
		DaysInMonthParser domParser = new DaysInMonthParser();
		MonthsParser monthParser = new MonthsParser();
		DaysInWeekParser dowParser = new DaysInWeekParser();
		
		seconds = smParser.parse(individuals[0]);
		minutes = smParser.parse(individuals[1]);
		hours = hParser.parse(individuals[2]);
		daysOfMonth = domParser.parse(individuals[3]);
		months = monthParser.parse(individuals[4]);
		daysOfWeek = dowParser.parse(individuals[5]);
		
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
		builder.append("]");
		return builder.toString();
	}
	
	
}
