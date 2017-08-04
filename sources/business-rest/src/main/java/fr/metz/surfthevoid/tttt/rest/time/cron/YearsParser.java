package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.YearsParser.YearsParsingResult;

public class YearsParser extends AbstractTimeParser<YearsParsingResult> {
	
	public static final String value = "(\\d+)";
	
	protected YearsParser() {
		super(value);
	}
	
	@Override
	protected YearsParsingResult newDayParsingResult() {
		return new YearsParsingResult();
	}

	@Override
	protected Integer getMaxTimeValue() {
		throw new NotImplementedException("There is not maximum value for years");
	}
	
	@Override
	protected Boolean extractTimeInterval(String value, YearsParsingResult parsingResult){
		Matcher tim = tip.matcher(value);
		if(tim.matches() && tim.reset().find()){
			int start = Integer.parseInt(tim.group(2));
			int itv = Integer.parseInt(tim.group(3));
			parsingResult.intervals.add(new YearInterval(start, itv));
			return true;
		}
		return false;
	}
	
	public static class YearsParsingResult extends BasicParsingResult {
		protected final TreeSet<YearInterval> intervals = new TreeSet<YearInterval>();

		public TreeSet<YearInterval> getIntervals() {
			return intervals;
		}
		
		@Override
		public Boolean isValid(LocalDateTime dateTime, ChronoField field){
			Integer year = dateTime.getYear();
			if(all) return true;
			if(intervals.stream().filter((itv) -> {
					int currentYear = year.intValue();
					return itv.startYear ==  currentYear || 
							(itv.startYear < currentYear && (currentYear - itv.startYear) % itv.interval == 0);
				}).findFirst().isPresent()){
				return true;
			}
			return values.contains(year);
		}
		
		@Override
		public LocalDateTime rollToNext(LocalDateTime dateTime, ChronoField field){
			Integer year = dateTime.getYear();
			if(all) return dateTime.plusYears(1);
			Integer nextValue = values.higher(year);
			Integer nextItvValue = nextInterval(year);
			if(nextItvValue != null){
				if(nextValue == null){
					nextValue = nextItvValue;
				} else {
					//return the lower element
					nextValue = nextItvValue - nextValue > 0 ? nextValue : nextItvValue;
				}
			}
			if(nextValue != null){
				return dateTime.withYear(nextValue);
			}
			return null;
		}
		
		@Override
		public LocalDateTime rollToPrevious(LocalDateTime dateTime, ChronoField field){
			Integer year = dateTime.getYear();
			if(all) return dateTime.minusYears(1);
			Integer previousValue = values.lower(year);
			Integer previousItvValue = previousInterval(year);
			if(previousItvValue != null){
				if(previousValue == null){
					previousValue = previousItvValue;
				} else {
					previousValue = previousItvValue - previousValue > 0 ? previousItvValue : previousValue;
				}
			}
			if(previousValue != null){
				return dateTime.withYear(previousValue);
			}
			return null;
		}
		
		protected Integer nextInterval(Integer value){
			TreeSet<Integer> itvValues = intervals.stream()
			.map(itv -> {
				//if: 2010/6 && value == 2010 => result: 2010 + 6 = 2016
				//if: 2010/6 && value == 2046 => modulo: (2046-2010)%6=0 => result: 2046-0+6=2052
				//if: 2010/6 && value == 2047 => modulo: (2047-2010)%6=1 => result: 2047-1+6=2052
				//if: 2010/6 && value == 1980 => result: 2010
				int gap = value - itv.startYear;
				if(gap == 0) return value + itv.interval;
				if(gap > 0){
					int modulo = gap % itv.interval;
					return value - modulo + itv.interval;
				} else {
					return itv.startYear;
				}
			}).collect(Collectors.toCollection(() -> new TreeSet<Integer>()));
			if(itvValues.isEmpty()) return null;
			// return the lower element
			return itvValues.first();
		}
		
		protected Integer previousInterval(Integer value){
			TreeSet<Integer> itvValues = intervals.stream()
			.filter(itv -> value > itv.startYear)
			.map(itv -> {
				//if: 2010/6 && value == 2046 => modulo: (2046-2010)%6=0 => result: 2046-6=2040
				//if: 2010/6 && value == 2047 => modulo: (2047-2010)%6=1 => result: 2047-1=2046
				int modulo = value - itv.startYear % itv.interval;
				if(modulo == 0){
					return value - itv.interval;
				}
				return value - modulo;
			}).collect(Collectors.toCollection(() -> new TreeSet<Integer>()));
			if(itvValues.isEmpty()) return null;
			// return the greater element
			return itvValues.last();
		}
		

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("YearsParsingResult [intervals=");
			builder.append(intervals);
			builder.append(", all=");
			builder.append(all);
			builder.append(", values=");
			builder.append(values);
			builder.append("]");
			return builder.toString();
		}

		@Override
		protected TreeSet<Integer> getAllPermittedValues(LocalDateTime dateTime) {
			throw new NotImplementedException("All values are permitted");
		}
	}
	
	public static class YearInterval implements Comparable<YearInterval>{
		private final int startYear;
		private final int interval;
		
		protected YearInterval(int startYear, int interval){
			this.startYear = startYear;
			this.interval = interval;
		}

		public int getStartYear() {
			return startYear;
		}

		public int getInterval() {
			return interval;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("YearInterval [startYear=");
			builder.append(startYear);
			builder.append(", interval=");
			builder.append(interval);
			builder.append("]");
			return builder.toString();
		}

		@Override
		public int compareTo(YearInterval arg0) {
			return startYear - arg0.startYear;
		}
	}
}
