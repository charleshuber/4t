package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.quartz.CronExpression;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInMonthParser.DaysInMonthParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInWeekParser.DaysInWeekParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.YearsParser.YearsParsingResult;

public class CronExpressionAnalyser {
	
	protected String expression;
	protected BasicParsingResult seconds;
    protected BasicParsingResult minutes;
    protected BasicParsingResult hours;
    protected DaysInMonthParsingResult daysOfMonth;
    protected BasicParsingResult months;
    protected DaysInWeekParsingResult daysOfWeek;
    protected YearsParsingResult years;
	
	public static void main(String...strings) throws ParseException{
		/*
		a.parse("0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * *");
		
		a.parse("* * * LW * *");
		*/
		
		List<String> expressions = Arrays.asList(
		"0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * ? 2001",
		"5-27 10/5  23 ? * L-2 2001,2003,2440-2444,2100/3,2102/4",
		"10/5 5-27 12,21,2-4,3/5 ? * MONL 2440-2444",
		"0,1,40,5-7,5/6 10/5,7,15/3 10/5 ? * MON#3 2100/3");
		
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
			System.out.println("");
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
