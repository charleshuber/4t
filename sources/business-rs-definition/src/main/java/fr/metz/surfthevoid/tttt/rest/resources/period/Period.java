package fr.metz.surfthevoid.tttt.rest.resources.period;

import java.util.Date;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class Period extends Resource {
	
	public static final String START_TIME_FIELD_NAME = "startTime";
	public static final String END_TIME_FIELD_NAME = "endTime";
	
	private Date startTime;
	private Date endTime;
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
