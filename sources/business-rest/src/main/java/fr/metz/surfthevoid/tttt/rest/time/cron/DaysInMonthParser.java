package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaysInMonthParser extends AbstractTimeParser {
	
	public static final String value = "([0-9]|1[0-9]|2[0-9]|3[0-1])";
	public static final String lastDayOfMonth = "L";
	public static final String lastWeekDayOfMonth = "LW";
	public static final String beforeLastDayOfMonth = "L\\-" + value;
	public static final String nearestWeekday = value + "W"; 
	
	protected Pattern bldomp = Pattern.compile(beforeLastDayOfMonth);
	protected Pattern nwdp = Pattern.compile(nearestWeekday);
	protected List<SimpleEntry<String, String>> dayMap = Arrays.asList(
		new SimpleEntry<String, String>("SUN", "1"),
	    new SimpleEntry<String, String>("MON", "2"),
	    new SimpleEntry<String, String>("TUE", "3"),
	    new SimpleEntry<String, String>("WED", "4"),
	    new SimpleEntry<String, String>("THU", "5"),
	    new SimpleEntry<String, String>("FRI", "6"),
	    new SimpleEntry<String, String>("SAT", "7")
	);
	
	protected DaysInMonthParser() {
		super(value);
	}
	
	@Override
	public DaysInMonthParsingResult parse(String value){
		value = convertDayNamesToPosition(value);
		Integer lastdayOffset = null;
		Integer nearestWeekday = null;
		DaysInMonthParsingResult result = new DaysInMonthParsingResult();
		if(lastDayOfMonth.equals(value)){
			result.lastDayOfMonth = true;
		} else if(lastWeekDayOfMonth.equals(value)){
			result.lastWeekday = true;
		} else if((lastdayOffset = getBeforeLastDayOfMonth(value)) != null){
			result.lastdayOffset = lastdayOffset;
		} else if((nearestWeekday = getNearestWeekday(value)) != null){
			result.nearestWeekday = nearestWeekday;
		} else {
			result.values = extractTimeValues(value);
		}
		return result;
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 31;
	}
	
	protected Boolean isIgnore(String value) {
		return super.isIgnore(value) || value.equals("?");
	}
	
	protected Integer getNearestWeekday(String value){
		Matcher nwdm =  nwdp.matcher(value);
		if(nwdm.matches() && nwdm.reset().find()){
			return Integer.parseInt(nwdm.group(1));
		}
		return null;
	}
	
	protected Integer getBeforeLastDayOfMonth(String value){
		Matcher bldompm =  bldomp.matcher(value);
		if(bldompm.matches() && bldompm.reset().find()){
			return Integer.parseInt(bldompm.group(1));
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
	
	public static class DaysInMonthParsingResult extends ParsingResult {
		private Boolean lastDayOfMonth;
		private Boolean lastWeekday;
		private Integer nearestWeekday;
		private Integer lastdayOffset;
		
		public Boolean getLastDayOfMonth() {
			return lastDayOfMonth;
		}
		public Integer getNearestWeekday() {
			return nearestWeekday;
		}
		public Boolean getLastWeekday() {
			return lastWeekday;
		}
		public Integer getLastdayOffset() {
			return lastdayOffset;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("DaysInMonthParsingResult [lastDayOfMonth=");
			builder.append(lastDayOfMonth);
			builder.append(", lastWeekday=");
			builder.append(lastWeekday);
			builder.append(", nearestWeekday=");
			builder.append(nearestWeekday);
			builder.append(", lastdayOffset=");
			builder.append(lastdayOffset);
			builder.append(", values=");
			builder.append(values);
			builder.append("]");
			return builder.toString();
		}
	}
}
