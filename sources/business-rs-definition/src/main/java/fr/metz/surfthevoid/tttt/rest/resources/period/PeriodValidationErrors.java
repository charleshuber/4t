package fr.metz.surfthevoid.tttt.rest.resources.period;

public enum PeriodValidationErrors {
	
	PERIOD_IS_INVALID("period.invalid", "The start time must be before the end time"),
	START_TIME_IS_MANDATORY("period.startTime.cannot.be.empty", "The start time of the period cannot be empty"),
	END_TIME_IS_MANDATORY("period.endTime.cannot.be.empty", "The end time of the period cannot be empty");
	
	PeriodValidationErrors(String code, String description){
		this.code = code;
		this.description = description;
	}
	
	private String code;
	private String description;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
