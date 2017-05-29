package fr.metz.surfthevoid.tttt.rest.resources.user;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class User extends Resource {
	
	public static final String EMAIL_FIELD_NAME = "email";
	public static final String LASTNAME_FIELD_NAME = "lastName";
	public static final String FIRSTNAME_FIELD_NAME = "firstName";
	public static final String NEW_PASSWORD_FIELD_NAME = "newPassword";
	public static final String NEW_PASSWORD_CHECK_FIELD_NAME = "newPasswordCheck";
	
	private String email;
	private String lastName;
	private String firstName;
	private String newPassword;
	private String newPasswordCheck;
		
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewPasswordCheck() {
		return newPasswordCheck;
	}
	public void setNewPasswordCheck(String newPasswordCheck) {
		this.newPasswordCheck = newPasswordCheck;
	}
	
}
