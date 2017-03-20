package fr.metz.surfthevoid.tttt.rest.resources.user;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class User extends Resource {
	private String lastName;
	private String firstName;
	
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
}
