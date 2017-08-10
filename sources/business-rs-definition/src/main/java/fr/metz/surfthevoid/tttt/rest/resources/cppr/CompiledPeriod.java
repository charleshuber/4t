package fr.metz.surfthevoid.tttt.rest.resources.cppr;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class CompiledPeriod extends Resource {
	
	public static final String NAME_FIELD_NAME = "name";
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
