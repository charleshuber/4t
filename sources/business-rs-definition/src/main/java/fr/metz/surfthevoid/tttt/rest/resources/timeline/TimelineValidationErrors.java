package fr.metz.surfthevoid.tttt.rest.resources.timeline;

public enum TimelineValidationErrors {
	
	NAME_IS_MANDATORY("timeline.name.cannot.be.empty", "The name of the timeline cannot be empty"),
	NAME_IS_ALREADY_USED("timeline.name.already.used", "This timeline is used by another group");
	
	TimelineValidationErrors(String code, String description){
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
