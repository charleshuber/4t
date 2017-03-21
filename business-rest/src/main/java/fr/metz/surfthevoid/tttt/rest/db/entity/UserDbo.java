package fr.metz.surfthevoid.tttt.rest.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class UserDbo extends GenericDbo {
	
	@Column(name="EMAIL", unique=true, nullable=false)
	private String email;
	@Column(name="PWD", nullable=false)
	private String password;
	@Column(name="LASTNAME")
	private String lastName;
	@Column(name="FIRSTNAME")
	private String firstName;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
}
