package fr.metz.surfthevoid.tttt.rest.time.cron;

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
		public Boolean isValid(Integer value){
			if(all) return true;
			if(intervals.stream().filter((itv) -> {
					return itv.startYear == value || 
							(itv.startYear < value && (value - itv.startYear) % itv.interval == 0);
				}).findFirst().isPresent()){
				return true;
			}
			return values.contains(value);
		}
		
		@Override
		public Integer next(Integer value){
			if(all) return value + 1;
			Integer nextItvValue = nextInterval(value);
			Integer nextValue = values.higher(value);
			if(nextItvValue != null){
				if(nextValue == null){
					return nextItvValue;
				}
				//return the lower element
				return nextItvValue - nextValue > 0 ? nextValue : nextItvValue;
			}
			return nextValue;
		}
		
		@Override
		public Integer previous(Integer value){
			if(all) return -1;
			Integer previousItvValue = previousInterval(value);
			Integer previousValue = values.lower(value);
			if(previousItvValue != null){
				if(previousValue == null){
					return previousItvValue;
				}
				//return the greater element
				return previousItvValue - previousValue > 0 ? previousItvValue : previousValue;
			}
			return previousValue;
		}
		
		protected Integer nextInterval(Integer value){
			TreeSet<Integer> itvValues = intervals.stream()
			.filter(itv -> value > itv.startYear)
			.map(itv -> {
				//if: 2010/6 && value == 2046 => modulo: (2046-2010)%6=0 => result: 2046-0+6=2052
				//if: 2010/6 && value == 2047 => modulo: (2047-2010)%6=1 => result: 2047-1+6=2052
				int modulo = (value - itv.startYear) % itv.interval;
				return value - modulo + itv.interval;
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
				int modulo = (value - itv.startYear) % itv.interval;
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
	}
	
	public static class YearInterval implements Comparable<YearInterval>{
		private final Integer startYear;
		private final Integer interval;
		
		protected YearInterval(Integer startYear, Integer interval){
			this.startYear = startYear;
			this.interval = interval;
		}

		public Integer getStartYear() {
			return startYear;
		}

		public Integer getInterval() {
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
			return startYear.compareTo(arg0.startYear);
		}
	}
}
