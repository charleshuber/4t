package fr.metz.surfthevoid.tttt.rest.resources.group;

public enum GroupValidationErrors {
	
	NAME_IS_MANDATORY("group.name.cannot.be.empty", "The name of the group cannot be empty"),
	NAME_IS_ALREADY_USED("group.name.already.used", "This name is used by another group"),
	CYCLIC_DEPENDENCY("group.cyclicdependency", "Group tree cannot content cyclic dependency");;
	
	GroupValidationErrors(String code, String description){
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
