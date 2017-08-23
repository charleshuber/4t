package fr.metz.surfthevoid.tttt.rest.resources.cronperiod;

import java.time.Duration;
import java.time.Period;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class CronPeriod extends Resource {
	
	public static final String EXP_SECONDS_FIELD = "expSecondes";
	public static final String EXP_MINUTES_FIELD = "expMinutes";
	public static final String EXP_HOURS_FIELD = "expHours";
	public static final String EXP_DAYSOFMONTH_FIELD = "expDaysOfMonths";
	public static final String EXP_MONTH_FIELD = "expMonths";
	public static final String EXP_DAYSOFWEEK_FIELD = "expDaysOfWeeks";
	public static final String EXP_YEARS_FIELD = "expYears";
		
	public static final String SECONDS_FIELD = "seconds";
	public static final String MINUTES_FIELD = "minutes";
	public static final String HOURS_FIELD = "hours";
	public static final String DAYS_FIELD = "days";
	public static final String MONTHS_FIELD = "months";	
	public static final String YEARS_FIELD = "years";
	
	private String expSeconds;
	private String expMinutes;
	private String expHours;
	private String expDaysOfMonths;
	private String expMonths;
	private String expDaysOfWeeks;
	private String expYears;
		
	private Long seconds;
	private Long minutes;
	private Long hours;
	private Long days;
	private Long months;	
	private Long years;
	
	public String getExpSeconds() {
		return expSeconds;
	}
	public void setExpSeconds(String expSeconds) {
		this.expSeconds = expSeconds;
	}
	public String getExpMinutes() {
		return expMinutes;
	}
	public void setExpMinutes(String expMinutes) {
		this.expMinutes = expMinutes;
	}
	public String getExpHours() {
		return expHours;
	}
	public void setExpHours(String expHours) {
		this.expHours = expHours;
	}
	public String getExpDaysOfMonths() {
		return expDaysOfMonths;
	}
	public void setExpDaysOfMonths(String expDaysOfMonths) {
		this.expDaysOfMonths = expDaysOfMonths;
	}
	public String getExpMonths() {
		return expMonths;
	}
	public void setExpMonths(String expMonths) {
		this.expMonths = expMonths;
	}
	public String getExpDaysOfWeeks() {
		return expDaysOfWeeks;
	}
	public void setExpDaysOfWeeks(String expDaysOfWeeks) {
		this.expDaysOfWeeks = expDaysOfWeeks;
	}
	public String getExpYears() {
		return expYears;
	}
	public void setExpYears(String expYears) {
		this.expYears = expYears;
	}
	
	public Long getSeconds() {
		return seconds;
	}
	public void setSeconds(Long seconds) {
		this.seconds = seconds;
	}
	public Long getMinutes() {
		return minutes;
	}
	public void setMinutes(Long minutes) {
		this.minutes = minutes;
	}
	public Long getHours() {
		return hours;
	}
	public void setHours(Long hours) {
		this.hours = hours;
	}
	public Long getDays() {
		return days;
	}
	public void setDays(Long days) {
		this.days = days;
	}
	public Long getMonths() {
		return months;
	}
	public void setMonths(Long months) {
		this.months = months;
	}
	public Long getYears() {
		return years;
	}
	public void setYears(Long years) {
		this.years = years;
	}
	
	public String toCronExpression(){
		StringBuilder sb = new StringBuilder();
		sb.append(expSeconds);
		sb.append(" " + expMinutes);
		sb.append(" " + expHours);
		sb.append(" " + expDaysOfMonths);
		sb.append(" " + expMonths);
		sb.append(" " + expDaysOfWeeks);
		if(StringUtils.isNotEmpty(expYears)){
			sb.append(" " + expYears);
		}
		return sb.toString();
	}
	
	public Duration toDuration(){
		Duration result = Duration.ZERO;
		if(seconds != null && seconds.longValue() > 0) result.plusSeconds(seconds);
		if(minutes != null && minutes.longValue() > 0) result.plusMinutes(minutes);
		if(hours != null && hours.longValue() > 0) result.plusHours(hours);
		if(days != null && days.longValue() > 0) result.plusDays(days);
		return result;
	}
	
	public Period toPeriod(){
		Period result = Period.ZERO;
		if(months != null && months.longValue() > 0) result.plusMonths(months);
		if(years != null && years.longValue() > 0) result.plusYears(years);
		return result;
	}
}
