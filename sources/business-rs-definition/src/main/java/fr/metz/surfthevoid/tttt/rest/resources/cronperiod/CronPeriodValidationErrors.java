package fr.metz.surfthevoid.tttt.rest.resources.cronperiod;

public enum CronPeriodValidationErrors {
	
	CRON_IS_INVALID("cron.invalid", "The cron expression is not valid"),
	PERIOD_IS_INVALID("period.invalid", "The duration is not valid");
	
	CronPeriodValidationErrors(String code, String description){
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
