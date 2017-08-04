package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
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
	
	private static final TreeSet<Integer> allPeriodValues = new TreeSet<>(Arrays.asList(
			0, 1, 2, 3, 4, 5, 6));
			
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
	protected void extractTimeValue(String value, DaysInWeekParsingResult parsingResult){
		value = new Integer(Integer.parseInt(value) - 1).toString();
		parsingResult.values.add(Integer.parseInt(value));
	}
	
	@Override
	protected Boolean extractTimeInterval(String value, DaysInWeekParsingResult parsingResult){
		Matcher tim = tip.matcher(value);
		if(tim.matches() && tim.reset().find()){
			int start = Integer.parseInt(tim.group(2)) - 1;
			int itv = Integer.parseInt(tim.group(3));
			for(int i=start; i <= getMaxTimeValue(); i+=itv){
				parsingResult.values.add(i);
			}
			return true;
		}
		return false;
	}

	@Override
	protected Integer getMaxTimeValue() {
		return allPeriodValues.last();
	}
	
	protected Integer getLastXOfMonth(String value){
		Matcher lomm =  lomp.matcher(value);
		if(lomm.matches() && lomm.reset().find()){
			return Integer.parseInt(lomm.group(1)) - 1;
		}
		return null;
	}
	
	protected DayPosition getDayPosition(String value){
		Matcher dpm =  dpp.matcher(value);
		if(dpm.matches() && dpm.reset().find()){
			return new DayPosition(Integer.parseInt(dpm.group(1)) - 1, Integer.parseInt(dpm.group(2)));
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
		public Boolean isValid(LocalDateTime dateTime, ChronoField field){
			if(all || unknown) return true;
			Integer value = dateTime.getDayOfWeek().getValue();
			if(lastDay){
				return value.equals(getAllPermittedValues(dateTime).last());
			} else if (lastDayOffset != null){
				Integer searchedValue = getAllPermittedValues(dateTime).last() - lastDayOffset;
				return value.equals(searchedValue);
			} else if(lastXOfMonth != null){
				return dateTime.getDayOfMonth() == getLastXOfMonth(dateTime).getDayOfMonth();
			} else if(dayPosition != null){
				return dateTime.getDayOfMonth() == getPositionedDay(dateTime).getDayOfMonth();
			}
			return values.contains(value);
		}
		
		@Override
		public LocalDateTime rollToNext(LocalDateTime dateTime, ChronoField field){
			if(unknown){
				return rollToNextDayOfWeek(dateTime, getAllPermittedValues(dateTime));
			} else if(lastDay){
				return rollToNextDayOfWeek(dateTime, getAllPermittedValues(dateTime).last());
			} else if (lastDayOffset != null){
				return rollToNextDayOfWeek(dateTime, getAllPermittedValues(dateTime).last() - lastDayOffset);
			} else if(lastXOfMonth != null){
				return getLastXOfMonth(dateTime);
			} else if(dayPosition != null){
				return getPositionedDay(dateTime);
			}
			return rollToNextDayOfWeek(dateTime, values);
		}

		@Override
		public LocalDateTime rollToPrevious(LocalDateTime dateTime, ChronoField field){
			if(unknown){
				return rollToPreviousDayOfWeek(dateTime, getAllPermittedValues(dateTime));
			} else if(lastDay){
				return rollToPreviousDayOfWeek(dateTime, getAllPermittedValues(dateTime).last());
			} else if (lastDayOffset != null){
				return rollToPreviousDayOfWeek(dateTime, getAllPermittedValues(dateTime).last() - lastDayOffset);
			} else if(lastXOfMonth != null){
				return getLastXOfMonth(dateTime);
			} else if(dayPosition != null){
				return getPositionedDay(dateTime);
			}
			return rollToPreviousDayOfWeek(dateTime, values);
		}
		
		protected LocalDateTime rollToNextDayOfWeek(LocalDateTime dateTime, TreeSet<Integer> values) {
			int currentDayOfWeek = dateTime.getDayOfWeek().getValue();
			int nextDayOfWeek = rollToNext(values, currentDayOfWeek);
			return rollToNextDayOfWeek(dateTime, nextDayOfWeek);
		}
		
		protected LocalDateTime rollToNextDayOfWeek(LocalDateTime dateTime, int nextDayOfWeek) {
			int currentMonth = dateTime.getMonthValue();
			LocalDateTime startDate = dateTime.plusDays(1);
			while(currentMonth == startDate.getMonthValue()){
				if(startDate.getDayOfWeek().getValue() == nextDayOfWeek){
					return startDate;
				}
				startDate = startDate.plusDays(1);
			}
			return null;
		}
		
		protected LocalDateTime rollToPreviousDayOfWeek(LocalDateTime dateTime, TreeSet<Integer> values) {
			int currentDayOfWeek = dateTime.getDayOfWeek().getValue();
			int previousDayOfWeek = rollToPrevious(values, currentDayOfWeek);
			return rollToPreviousDayOfWeek(dateTime, previousDayOfWeek);
		}
		
		protected LocalDateTime rollToPreviousDayOfWeek(LocalDateTime dateTime, int previousDayOfWeek) {
			int currentMonth = dateTime.getMonthValue();
			LocalDateTime startDate = dateTime.minusDays(1);
			while(currentMonth == startDate.getMonthValue()){
				if(startDate.getDayOfWeek().getValue() == previousDayOfWeek){
					return startDate;
				}
				startDate = startDate.minusDays(1);
			}
			return null;
		}
		
		protected LocalDateTime getLastXOfMonth(LocalDateTime dateTime){
			//go to the last day of the month
			LocalDateTime startDate = resetMonth(dateTime)
					.plusMonths(1)
					.minusDays(1);
			while(startDate.getDayOfWeek().getValue() != lastXOfMonth){
				startDate = startDate.minusDays(1);
			}
			return dateTime.withDayOfMonth(startDate.getDayOfMonth());
		}
		
		protected LocalDateTime getPositionedDay(LocalDateTime dateTime){
			//go to the first day of the month
			LocalDateTime startDate = resetMonth(dateTime);
			int currentPosition = 0;
			int currentMonth = startDate.getMonthValue();
			while(currentPosition < dayPosition.position 
					&& currentMonth == startDate.getMonthValue()){
				if(startDate.getDayOfWeek().getValue() == dayPosition.day){
					currentPosition++;
					if(currentPosition == dayPosition.position) break;
				}
				startDate = startDate.plusDays(1);
			}
			if(currentMonth == startDate.getMonthValue()){
				return dateTime.withDayOfMonth(startDate.getDayOfMonth());
			} 
			return null;
		}
		
		@Override
		protected TreeSet<Integer> getAllPermittedValues(LocalDateTime dateTime) {
			return allPeriodValues;
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
	
	public static class DayPosition {
		
		private final int day;
		private final int position;
		
		DayPosition(int day, int position){
			this.day = day;
			this.position = position;
		}

		public int getDay() {
			return day;
		}

		public int getPosition() {
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
