package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.ParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInMonthParser.DaysInMonthParsingResult;

public class CronExpressionAnalyser {
	
	protected transient ParsingResult seconds;
    protected transient ParsingResult minutes;
    protected transient ParsingResult hours;
    protected transient DaysInMonthParsingResult daysOfMonth;
    
    protected transient TreeSet<Integer> months;
    protected transient TreeSet<Integer> daysOfWeek;
    protected transient TreeSet<Integer> years;

    protected transient boolean lastdayOfWeek = false;
    protected transient int nthdayOfWeek = 0;
    protected transient boolean lastdayOfMonth = false;
    protected transient boolean nearestWeekday = false;
    protected transient int lastdayOffset = 0;
	
    public String timeunit = "[0-9]";
    public String timedecimal = "[1-5]";
    public String timevalue = "(" + timedecimal + "?" + timeunit + ")";
    public String timerange = "(" + timevalue + "-" + timevalue + ")";
    public String timeinterval = "(" + timevalue + "/" + timevalue + ")";
    public String timelistItem =  "(" + timevalue + "|" + timerange + "|" + timeinterval + ")";
    public String timelist = "(" + timelistItem + ",)+" + timelistItem;
    
    Pattern tlp = Pattern.compile(timelist);
	Pattern tip = Pattern.compile(timeinterval);
	Pattern trp = Pattern.compile(timerange);
	Pattern tvp = Pattern.compile(timevalue);
	
	public static void main(String...strings){
		CronExpressionAnalyser a = new CronExpressionAnalyser();
		
		a.parse("0,1,40,5-7,5/6 10/5,7,15/3 10/5 L-5 * *");
		
		a.parse("* * * LW * *");
	}
	
	public void parse(String cronExpression){
		String[] individuals = cronExpression.split("\\s+");
		if(individuals.length < 6){
			throw new IllegalArgumentException();
		}
		SecondsMinutesParser smParser = new SecondsMinutesParser();
		HoursParser hParser = new HoursParser();
		DaysInMonthParser domParser = new DaysInMonthParser();
		
		seconds = smParser.parse(individuals[0]);
		minutes = smParser.parse(individuals[1]);
		hours = hParser.parse(individuals[2]);
		daysOfMonth = domParser.parse(individuals[3]);
		
		System.out.println(seconds);
		System.out.println(minutes);
		System.out.println(hours);
		System.out.println(daysOfMonth);
		 
	}
	
	protected TreeSet<Integer> extractTimeValues(String value){
		TreeSet<Integer> results = new TreeSet<Integer>();
		if(isTimeList(value)){
			String[] values = value.split(",");
			for(int i=0; i< values.length; i++){
				results.addAll(extractTimelistItem(values[i]));
			}
		} else {
			extractTimelistItem(value);
		}
		return results;
	}
	
	protected List<Integer> extractTimelistItem(String value){
		if (isTimeInterval(value)){
			return extractTimeInterval(value);
		} else if(isTimeRange(value)){
			return extractTimeRange(value);
		} else if(isTimeValue(value)){
			return Arrays.asList(Integer.parseInt(value));
		}
		return new ArrayList<Integer>();
	}
	
	protected List<Integer> extractTimeInterval(String value){
		List<Integer> results = new ArrayList<Integer>();
		Matcher tim = tip.matcher(value);
		if(tim.find()){
			int start = Integer.parseInt(tim.group(2));
			int itv = Integer.parseInt(tim.group(3));
			for(int i=start; i <= 59; i+=itv){
				results.add(i);
			}
		}
		return results;
	}
	
	protected List<Integer> extractTimeRange(String value){
		List<Integer> results = new ArrayList<Integer>();
		Matcher trm = trp.matcher(value);
		if(trm.find()){
			int start = Integer.parseInt(trm.group(2));
			int end = Integer.parseInt(trm.group(3));
			for(int i=start; i <= end; i++){
				results.add(i);
			}
		}
		return results;
	}
	
	protected Boolean isTimeList(String value){
		return tlp.matcher(value).matches();
	}
	protected Boolean isTimeInterval(String value){
		return tip.matcher(value).matches();
	}
	protected Boolean isTimeRange(String value){
		return trp.matcher(value).matches();
	}
	protected Boolean isTimeValue(String value){
		return tvp.matcher(value).matches();
	}
}
