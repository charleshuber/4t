package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractDaysParser.DaysParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInWeekParser.DaysInWeekParsingResult;

public class DaysInWeekParser extends AbstractDaysParser<DaysInWeekParsingResult> {
	
	public static final String value = "([1-7])";
	
	public static final String lastXOfMonth = value + "L";
	public static final String dayPosition = value + "#([1-5])";
	
	protected Pattern lomp = Pattern.compile(lastXOfMonth);
	protected Pattern dpp = Pattern.compile(dayPosition);
	
	protected List<SimpleEntry<String, String>> dayMap = Arrays.asList(
			new SimpleEntry<String, String>("SUN", "1"),
		    new SimpleEntry<String, String>("MON", "2"),
		    new SimpleEntry<String, String>("TUE", "3"),
		    new SimpleEntry<String, String>("WED", "4"),
		    new SimpleEntry<String, String>("THU", "5"),
		    new SimpleEntry<String, String>("FRI", "6"),
		    new SimpleEntry<String, String>("SAT", "7"));
			
	protected DaysInWeekParser() {
		super(value);
	}
	
	@Override
	public DaysInWeekParsingResult parse(String value){
		value = convertDayNamesToPosition(value);
		return super.parse(value);
	}
	
	@Override
	protected DaysInWeekParsingResult newDayParsingResult() {
		return new DaysInWeekParsingResult();
	}

	@Override
	protected Boolean extractSpecificPattern(String value, DaysInWeekParsingResult result) {
		DayPosition dayPosition = null;
		Integer lastXOfMonth = null;
		if((lastXOfMonth = getLastXOfMonth(value)) != null){
			result.lastXOfMonth = lastXOfMonth;
			return true;
		} else if((dayPosition = getDayPosition(value)) != null){
			result.dayPosition = dayPosition;
			return true;
		}
		return false;
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 7;
	}
	
	protected Integer getLastXOfMonth(String value){
		Matcher lomm =  lomp.matcher(value);
		if(lomm.matches() && lomm.reset().find()){
			return Integer.parseInt(lomm.group(1));
		}
		return null;
	}
	
	protected DayPosition getDayPosition(String value){
		Matcher dpm =  dpp.matcher(value);
		if(dpm.matches() && dpm.reset().find()){
			return new DayPosition(Integer.parseInt(dpm.group(1)), Integer.parseInt(dpm.group(2)));
		}
		return null;
	}
	
	protected String convertDayNamesToPosition(String value){
		String result = value;
		for(SimpleEntry<String, String> entry : dayMap){
			result = result.replaceAll(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public static class DaysInWeekParsingResult extends DaysParsingResult {

		protected Integer lastXOfMonth;
		protected DayPosition dayPosition;
		
		public Integer getLastXOfMonth() {
			return lastXOfMonth;
		}
		public DayPosition getDayPosition() {
			return dayPosition;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("DaysInWeekParsingResult [lastXOfMonth=");
			builder.append(lastXOfMonth);
			builder.append(", dayPosition=");
			builder.append(dayPosition);
			builder.append(", unknown=");
			builder.append(unknown);
			builder.append(", lastDay=");
			builder.append(lastDay);
			builder.append(", lastDayOffset=");
			builder.append(lastDayOffset);
			builder.append(", all=");
			builder.append(all);
			builder.append(", values=");
			builder.append(values);
			builder.append("]");
			return builder.toString();
		}
		
	}
	
	public static class DayPosition{
		
		private final Integer day;
		private final Integer position;
		
		DayPosition(Integer day, Integer position){
			this.day = day;
			this.position = position;
		}

		public Integer getDay() {
			return day;
		}

		public Integer getPosition() {
			return position;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("DayPosition [day=");
			builder.append(day);
			builder.append(", position=");
			builder.append(position);
			builder.append("]");
			return builder.toString();
		}
	}
}
