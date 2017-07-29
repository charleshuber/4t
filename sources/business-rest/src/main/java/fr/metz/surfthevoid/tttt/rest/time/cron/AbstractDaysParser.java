package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractDaysParser.DaysParsingResult;

public abstract class AbstractDaysParser<T extends DaysParsingResult> extends AbstractTimeParser {
	
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
		T result = newDayParsingResult();
		if(unknown.equals(value)){
			result.unknown = true;
		} else if(lastDay.equals(value)){
			result.lastDay = true;
		} else if((lastdayOffset = getBeforeLastDay(value)) != null){
			result.lastDayOffset = lastdayOffset;
		} else if(!doSpecificTreatement(value, result)){
			result.values = extractTimeValues(value);
		}
		return result;
	}
	
	protected abstract T newDayParsingResult();
	protected abstract Boolean doSpecificTreatement(String value, T result);
	
	@Override
	protected Boolean isIgnore(String value) {
		return super.isIgnore(value) || value.equals(unknown);
	}
	
	protected Integer getBeforeLastDay(String value){
		Matcher bldm =  bldp.matcher(value);
		if(bldm.matches() && bldm.reset().find()){
			return Integer.parseInt(bldm.group(1));
		}
		return null;
	}
	
	public static abstract class DaysParsingResult extends ParsingResult {
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
