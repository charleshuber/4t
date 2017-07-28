package fr.metz.surfthevoid.tttt.rest.time;

import java.time.LocalDateTime;

public class TimeLine {
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private TimePeriod period;
	private String cronExpression;
	
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
	public TimePeriod getPeriod() {
		return period;
	}
	public void setPeriod(TimePeriod period) {
		this.period = period;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	
}
