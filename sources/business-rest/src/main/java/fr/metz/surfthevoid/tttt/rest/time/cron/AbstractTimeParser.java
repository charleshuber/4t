package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractTimeParser {
	
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
    
    public ParsingResult parse(String value){
    	ParsingResult parsingResult = new ParsingResult();
    	parsingResult.values = this.extractTimeValues(value);
    	return parsingResult;
    }
    
	protected TreeSet<Integer> extractTimeValues(String value){
		TreeSet<Integer> results = new TreeSet<Integer>();
		if(isIgnore(value)){
			return results;
		} else if(isTimeList(value)){
			String[] values = value.split(",");
			for(int i=0; i< values.length; i++){
				results.addAll(extractTimelistItem(values[i]));
			}
		} else {
			results.addAll(extractTimelistItem(value));
		}
		return results;
	}
	
	protected Boolean isIgnore(String value) {
		return value.equals("*");
	}

	protected List<Integer> extractTimelistItem(String value){
		if (isTimeInterval(value)){
			return extractTimeInterval(value);
		} else if(isTimeRange(value)){
			return extractTimeRange(value);
		} else if(isTimeValue(value)){
			return Arrays.asList(Integer.parseInt(value));
		}
		throw new IllegalArgumentException();
	}
	
	protected List<Integer> extractTimeInterval(String value){
		List<Integer> results = new ArrayList<Integer>();
		Matcher tim = tip.matcher(value);
		if(tim.find()){
			int start = Integer.parseInt(tim.group(2));
			int itv = Integer.parseInt(tim.group(3));
			for(int i=start; i <= getMaxTimeValue(); i+=itv){
				results.add(i);
			}
		}
		return results;
	}
	
	protected abstract Integer getMaxTimeValue();
	
	protected List<Integer> extractTimeRange(String value){
		List<Integer> results = new ArrayList<Integer>();
		Matcher trm = trp.matcher(value);
		if(trm.find()){
			int start = Integer.parseInt(trm.group(2));
			int end = Integer.parseInt(trm.group(3));
			for(int i=start; i <= end; i++){
				results.add(i);
			}
		}
		return results;
	}
	
	protected Boolean isTimeList(String value){
		return tlp.matcher(value).matches();
	}
	protected Boolean isTimeInterval(String value){
		return tip.matcher(value).matches();
	}
	protected Boolean isTimeRange(String value){
		return trp.matcher(value).matches();
	}
	protected Boolean isTimeValue(String value){
		return tvp.matcher(value).matches();
	}
	
	public static class ParsingResult {
		protected TreeSet<Integer> values;
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
