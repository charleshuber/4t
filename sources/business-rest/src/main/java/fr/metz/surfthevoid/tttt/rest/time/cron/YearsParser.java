package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.TreeSet;
import java.util.regex.Matcher;

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
			if(intervals.size() > 0){
				do the stuff
			}
			return values.contains(values);
		}
		
		@Override
		public Integer next(Integer value){
			if(all) return value + 1;
			Integer next = values.higher(value);
			return next == null ? values.first() : next;
		}
		
		@Override
		public Integer previous(Integer value){
			if(all) return -1;
			Integer previous = values.lower(value);
			return previous == null ? values.last() : previous;
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
