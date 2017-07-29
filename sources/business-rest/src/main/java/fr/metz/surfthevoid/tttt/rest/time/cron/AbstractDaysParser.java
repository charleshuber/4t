package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractDaysParser.DaysParsingResult;

public abstract class AbstractDaysParser<T extends DaysParsingResult> extends AbstractTimeParser<T> {
	
	public static final String unknown = "?";
	public static final String lastDay = "L";
	
	protected final String beforeLastDay;
	protected final Pattern bldp;
	
	protected AbstractDaysParser(String value) {
		super(value);
		beforeLastDay = lastDay + "\\-" + value;
		bldp = Pattern.compile(beforeLastDay);
	}
	
	@Override
	public T parse(String value){
		Integer lastdayOffset = null;
		T parsingResults = newDayParsingResult();
		if(unknown.equals(value)){
			parsingResults.unknown = true;
		} else if(lastDay.equals(value)){
			parsingResults.lastDay = true;
		} else if((lastdayOffset = getBeforeLastDay(value)) != null){
			parsingResults.lastDayOffset = lastdayOffset;
		} else if(!extractSpecificPattern(value, parsingResults)){
			extractTimeValues(value, parsingResults);
		}
		return parsingResults;
	}
	
	protected abstract Boolean extractSpecificPattern(String value, T result);
	
	@Override
	protected Boolean isIgnored(String value) {
		return super.isIgnored(value) || value.equals(unknown);
	}
	
	protected Integer getBeforeLastDay(String value){
		Matcher bldm =  bldp.matcher(value);
		if(bldm.matches() && bldm.reset().find()){
			return Integer.parseInt(bldm.group(1));
		}
		return null;
	}
	
	public static abstract class DaysParsingResult extends BasicParsingResult {
		protected Boolean unknown;
		protected Boolean lastDay;
		protected Integer lastDayOffset;
		
		public Boolean getUnknown() {
			return unknown;
		}
		public Boolean getLastDay() {
			return lastDay;
		}
		public Integer getLastDayOffset() {
			return lastDayOffset;
		}	
	}
}
