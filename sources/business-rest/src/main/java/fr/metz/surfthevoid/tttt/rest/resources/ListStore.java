package fr.metz.surfthevoid.tttt.rest.resources;

import java.util.Set;
import java.util.stream.Collectors;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;

public abstract class ListStore<R extends Resource, T extends GenericDbo> extends ResourceStore<R, T>{

	public Set<R> readAll() throws ValidationException {
		return getDao().readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toSet());
	}
}
