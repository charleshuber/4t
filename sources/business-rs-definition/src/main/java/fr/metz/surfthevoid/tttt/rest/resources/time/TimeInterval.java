package fr.metz.surfthevoid.tttt.rest.resources.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class TimeInterval {
	
	private Date startTime;
	private Date endTime;
	
	public TimeInterval(){}
	
	public TimeInterval(LocalDateTime start, LocalDateTime end){
		this.startTime = Date.from(start.toInstant(ZoneOffset.UTC));
		this.endTime = Date.from(end.toInstant(ZoneOffset.UTC));
	}
	
	public TimeInterval(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
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
