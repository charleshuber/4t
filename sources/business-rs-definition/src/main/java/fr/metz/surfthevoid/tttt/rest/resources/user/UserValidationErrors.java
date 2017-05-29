package fr.metz.surfthevoid.tttt.rest.resources.user;

public enum UserValidationErrors {
	
	EMAIL_IS_MANDATORY("user.email.cannot.be.empty", "The user's email cannot be empty"),
	EMAIL_IS_ALREADY_USED("user.email.already.used", "This email is used by another user"),
	PWD_IS_MANDATORY("user.password.cannot.be.empty", "The user's password cannot be empty"),
	PWD_CHECK_IS_MANDATORY("user.password.check.cannot.be.empty", "The user's password verification cannot be empty"),
	PWD_CHECK_IS_NOT_VALID("user.password.check.cannot.be.empty", "The user's password verification cannot be empty"),
	LASTNAME_IS_MANDATORY("user.lastname.cannot.be.empty", "The user's lastname cannot be empty"),
	FIRSTNAME_IS_MANDATORY("user.firstname.cannot.be.empty", "The user's firstname cannot be empty");
	
	UserValidationErrors(String code, String description){
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
