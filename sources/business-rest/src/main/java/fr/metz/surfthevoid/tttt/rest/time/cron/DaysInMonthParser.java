package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;
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
	protected Boolean extractSpecificPattern(String value, DaysInMonthParsingResult result) {
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
		public Boolean isValid(LocalDateTime dateTime, ChronoField field){
			if(all || unknown) return true;
			int value = dateTime.getDayOfMonth();
			if(lastDay){
				return new Integer(value).equals(getAllPermittedValues(dateTime).last());
			} else if (lastDayOffset != null){
				
			} else if(lastWeekday){
				
			} else if(nearestWeekday != null){
				
			}
			return values.contains(value);
		}
		
		@Override
		public LocalDateTime rollToNext(LocalDateTime dateTime, ChronoField field){
			int value = dateTime.getDayOfMonth();
			if(unknown){
				Integer next = rollToNext(getAllPermittedValues(dateTime), value);
				return dateTime.withDayOfMonth(next);
			} else if(lastDay){
				Integer lastDayValue = getAllPermittedValues(dateTime).last();
				return dateTime.withDayOfMonth(lastDayValue);
			} else if (lastDayOffset != null){
				return getLastDayOffsetDate(dateTime);
			} else if(lastWeekday){
				return getLastDayOfWeekDate(dateTime);
			} else if(nearestWeekday != null){
				Integer next = rollToNext(getAllPermittedValues(dateTime), value);
				dateTime = dateTime.withDayOfMonth(next);
				while(dateTime.getDayOfWeek().getValue() == 1 
						|| dateTime.getDayOfWeek().getValue() == 7){
					hrere
				}
				return dateTime;
			}
			return super.rollToNext(dateTime, field);
		}
		
		@Override
		public LocalDateTime rollToPrevious(LocalDateTime dateTime, ChronoField field){
			int value = dateTime.getDayOfMonth();
			if(unknown){
				Integer previous = rollToPrevious(getAllPermittedValues(dateTime), value);
				return dateTime.withDayOfMonth(previous);
			} else if(lastDay){
				Integer lastDayValue = getAllPermittedValues(dateTime).last();
				return dateTime.withDayOfMonth(lastDayValue);
			} else if (lastDayOffset != null){
				return getLastDayOffsetDate(dateTime);
			} else if(lastWeekday){
				return getLastDayOfWeekDate(dateTime);
			} else if(nearestWeekday != null){
				Integer previous = rollToPrevious(getAllPermittedValues(dateTime), value);
				dateTime = dateTime.withDayOfMonth(previous);
				while(dateTime.getDayOfWeek().getValue() == 1 
						|| dateTime.getDayOfWeek().getValue() == 7){
					heare
				}
				return dateTime;
			}
			return super.rollToPrevious(dateTime, field);
		}
		
		private LocalDateTime getLastDayOfWeekDate(LocalDateTime dateTime){
			Integer lastDayValue = getAllPermittedValues(dateTime).last();
			LocalDateTime lastDayWeek = dateTime.withDayOfMonth(lastDayValue);
			while(lastDayWeek.getDayOfWeek().getValue() == 1 
					|| lastDayWeek.getDayOfWeek().getValue() == 7){
				lastDayWeek = lastDayWeek.minusDays(1);
			}
			return lastDayWeek;
		}
		
		private LocalDateTime getLastDayOffsetDate(LocalDateTime dateTime){
			Integer beforeLastDay = getAllPermittedValues(dateTime).last() - lastDayOffset;
			// If the searched day does not exist we do not manipulate the date
			if(beforeLastDay < 1){
				return dateTime;
			}
			return dateTime.withDayOfMonth(beforeLastDay);
		}
		
		@Override
		protected TreeSet<Integer> getAllPermittedValues(LocalDateTime dateTime) {
			long numberOfDaysInCurrentMonth = Duration.between(dateTime.truncatedTo(ChronoUnit.MONTHS), 
					dateTime.truncatedTo(ChronoUnit.MONTHS).plusMonths(1)).toDays();
			TreeSet<Integer> daysInCurrentMonth = new TreeSet<>();
			for(int i=1; i <= numberOfDaysInCurrentMonth; i++){
				daysInCurrentMonth.add(i);
			}
			return daysInCurrentMonth;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("DaysInMonthParsingResult [lastWeekday=");
			builder.append(lastWeekday);
			builder.append(", nearestWeekday=");
			builder.append(nearestWeekday);
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
}
