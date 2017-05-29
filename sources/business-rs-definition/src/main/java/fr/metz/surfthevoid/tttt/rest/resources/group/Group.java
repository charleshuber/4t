package fr.metz.surfthevoid.tttt.rest.resources.group;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class Group extends Resource {
	
	public static final String NAME_FIELD_NAME = "name";
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
