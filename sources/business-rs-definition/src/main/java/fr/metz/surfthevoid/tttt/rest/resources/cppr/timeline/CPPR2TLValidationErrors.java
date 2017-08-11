package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

public enum CPPR2TLValidationErrors {
	
	ORDER_IS_MANDATORY("cppr2tl.order.cannot.be.empty", "The order of the link cannot be empty"),
	ORDER_IS_ALREADY_USED("cppr2tl.order.already.used", "This order is already used by another link"),
	CPPR_INVALID("cppr2tl.compiledPeriodId.invalid", "The compiled period id is not valid"),
	TIMELINE_INVALID("cppr2tl.timelineId.invalid", "The timeline id is not valid");
	
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
