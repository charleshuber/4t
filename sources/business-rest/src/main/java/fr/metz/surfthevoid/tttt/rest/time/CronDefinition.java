package fr.metz.surfthevoid.tttt.rest.time;

public class CronDefinition {
	
	private TimeDuration period;
	private String cronExpression;
	
	public TimeDuration getPeriod() {
		return period;
	}
	public void setPeriod(TimeDuration period) {
		this.period = period;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
}
