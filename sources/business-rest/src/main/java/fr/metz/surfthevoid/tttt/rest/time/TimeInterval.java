package fr.metz.surfthevoid.tttt.rest.time;

import java.time.LocalDateTime;

public class TimeInterval {
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public TimeInterval(){}
	
	public TimeInterval(LocalDateTime startTime, LocalDateTime endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
		.append("TimeInterval [startTime=")
		.append(startTime)
		.append(", endTime=")
		.append(endTime).append("]");
		return builder.toString();
	}
		
}
