package fr.metz.surfthevoid.tttt.rest.time;

import java.time.LocalDateTime;
import java.util.List;

public class TimeLine {
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private List<TimeInterval> intervals;
	private List<CronDefinition> cronDefintions;
	
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
	
	public List<TimeInterval> getIntervals() {
		return intervals;
	}
	public void setIntervals(List<TimeInterval> intervals) {
		this.intervals = intervals;
	}
	public List<CronDefinition> getCronDefintions() {
		return cronDefintions;
	}
	public void setCronDefintions(List<CronDefinition> cronDefintions) {
		this.cronDefintions = cronDefintions;
	}
}
