package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

public enum CPPR2TLValidationErrors {
	
	NAME_IS_MANDATORY("cppr.name.cannot.be.empty", "The name of the compiled period cannot be empty"),
	NAME_IS_ALREADY_USED("cppr.name.already.used", "This name is used by another compiled period");
	
	CPPR2TLValidationErrors(String code, String description){
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
