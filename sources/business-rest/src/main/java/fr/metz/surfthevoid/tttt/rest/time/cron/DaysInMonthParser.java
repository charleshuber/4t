package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractDaysParser.DaysParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.DaysInMonthParser.DaysInMonthParsingResult;

public class DaysInMonthParser extends AbstractDaysParser<DaysInMonthParsingResult> {
	
	public static final String value = "([0-9]|1[0-9]|2[0-9]|3[0-1])";
	public static final String lastWeekDayOfMonth = "LW";
	public static final String nearestWeekday = value + "W";
	
	protected Pattern nwdp = Pattern.compile(nearestWeekday);
	
	protected DaysInMonthParser() {
		super(value);
	}
	
	@Override
	protected DaysInMonthParsingResult newDayParsingResult() {
		return new DaysInMonthParsingResult();
	}

	@Override
	protected Boolean doSpecificTreatement(String value, DaysInMonthParsingResult result) {
		Integer nearestWeekday = null;
		if(lastWeekDayOfMonth.equals(value)){
			result.lastWeekday = true;
			return true;
		} else if((nearestWeekday = getNearestWeekday(value)) != null){
			result.nearestWeekday = nearestWeekday;
			return true;
		}
		return false;
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 31;
	}
	
	protected Integer getNearestWeekday(String value){
		Matcher nwdm =  nwdp.matcher(value);
		if(nwdm.matches() && nwdm.reset().find()){
			return Integer.parseInt(nwdm.group(1));
		}
		return null;
	}
	
	public static class DaysInMonthParsingResult extends DaysParsingResult {

		protected Boolean lastWeekday;
		protected Integer nearestWeekday;
		
		public Integer getNearestWeekday() {
			return nearestWeekday;
		}
		public Boolean getLastWeekday() {
			return lastWeekday;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("DaysInMonthParsingResult [lastDayOfMonth=");
			builder.append(lastDay);
			builder.append(", lastWeekday=");
			builder.append(lastWeekday);
			builder.append(", nearestWeekday=");
			builder.append(nearestWeekday);
			builder.append(", lastdayOffset=");
			builder.append(lastDayOffset);
			builder.append(", values=");
			builder.append(values);
			builder.append("]");
			return builder.toString();
		}
	}
}
