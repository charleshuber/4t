package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;

public abstract class AbstractTimeParser<T extends BasicParsingResult> {
	
    protected String timevalue;
    protected String timerange;
    protected String timeinterval;
    protected String timelistItem;
    protected String timelist;
	protected Pattern tlp;
	protected Pattern tip;
	protected Pattern trp;
	protected Pattern tvp;
    
    protected AbstractTimeParser(String timevalue){    
        this.timevalue = timevalue;
        this.timerange = "(" + timevalue + "-" + timevalue + ")";
        this.timeinterval = "(" + timevalue + "/" + timevalue + ")";
        this.timelistItem =  "(" + timevalue + "|" + timerange + "|" + timeinterval + ")";
        this.timelist = "(" + timelistItem + ",)+" + timelistItem;
        
        this.tlp = Pattern.compile(timelist);
    	this.tip = Pattern.compile(timeinterval);
    	this.trp = Pattern.compile(timerange);
    	this.tvp = Pattern.compile(timevalue);
    }
    
    public T parse(String value){
    	T parsingResult = newDayParsingResult();
    	this.extractTimeValues(value, parsingResult);
    	return parsingResult;
    }
    
	protected void extractTimeValues(String value, T parsingResult){
		if(isIgnored(value)){
			return;
		} else if(isTimeList(value)){
			String[] values = value.split(",");
			for(int i=0; i< values.length; i++){
				extractTimelistItem(values[i], parsingResult);
			}
		} else {
			extractTimelistItem(value, parsingResult);
		}
	}
	
	protected Boolean isIgnored(String value) {
		return value.equals("*");
	}

	protected void extractTimelistItem(String value, T parsingResult){
		if (extractTimeInterval(value, parsingResult)){
			return;
		} else if(extractTimeRange(value, parsingResult)){
			return;
		} else if(isTimeValue(value)){
			parsingResult.values.add(Integer.parseInt(value));
			return;
		}
		throw new IllegalArgumentException();
	}
	
	protected Boolean extractTimeInterval(String value, T parsingResult){
		Matcher tim = tip.matcher(value);
		if(tim.matches() && tim.reset().find()){
			int start = Integer.parseInt(tim.group(2));
			int itv = Integer.parseInt(tim.group(3));
			for(int i=start; i <= getMaxTimeValue(); i+=itv){
				parsingResult.values.add(i);
			}
			return true;
		}
		return false;
	}
	
	protected Boolean extractTimeRange(String value, T parsingResult){
		Matcher trm = trp.matcher(value);
		if(trm.matches() && trm.reset().find()){
			int start = Integer.parseInt(trm.group(2));
			int end = Integer.parseInt(trm.group(3));
			for(int i=start; i <= end; i++){
				parsingResult.values.add(i);
			}
			return true;
		}
		return false;
	}
	
	protected Boolean isTimeList(String value){
		return tlp.matcher(value).matches();
	}
	protected Boolean isTimeValue(String value){
		return tvp.matcher(value).matches();
	}
	
	protected abstract Integer getMaxTimeValue();
	protected abstract T newDayParsingResult();
	
	public static class BasicParsingResult {
		protected final TreeSet<Integer> values = new TreeSet<Integer>();
		public TreeSet<Integer> getValues() {
			return values;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ParsingResult [values=");
			builder.append(values);
			builder.append("]");
			return builder.toString();
		}
	}
}