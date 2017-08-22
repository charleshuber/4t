package fr.metz.surfthevoid.tttt.rest.resources.cronperiod;

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
	
	private String expSecondes;
	private String expMinutes;
	private String expHours;
	private String expDaysOfMonths;
	private String expMonths;
	private String expDaysOfWeeks;
	private String expYears;
		
	private String seconds;
	private String minutes;
	private String hours;
	private String days;
	private String months;	
	private String years;
	
	public String getExpSecondes() {
		return expSecondes;
	}
	public void setExpSecondes(String expSecondes) {
		this.expSecondes = expSecondes;
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
	public String getSeconds() {
		return seconds;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	public String getMinutes() {
		return minutes;
	}
	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
}
