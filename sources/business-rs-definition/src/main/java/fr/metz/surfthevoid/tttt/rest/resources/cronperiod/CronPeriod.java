package fr.metz.surfthevoid.tttt.rest.resources.cronperiod;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class CronPeriod extends Resource {
	
	public static final String START_TIME_FIELD_NAME = "startTime";
	public static final String END_TIME_FIELD_NAME = "endTime";
	
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
}
