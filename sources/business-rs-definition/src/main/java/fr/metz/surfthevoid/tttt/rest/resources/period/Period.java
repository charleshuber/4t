package fr.metz.surfthevoid.tttt.rest.resources.period;

import java.time.LocalDateTime;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class Period extends Resource {
	
	public static final String START_TIME_FIELD_NAME = "startTime";
	public static final String END_TIME_FIELD_NAME = "endTime";
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
}
