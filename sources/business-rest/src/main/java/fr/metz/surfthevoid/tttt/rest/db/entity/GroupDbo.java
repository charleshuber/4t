package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="GROUP")
public class GroupDbo extends GroupableDbo {
	
	public String name;
	
	@ManyToMany
	@JoinTable(name="GROUP_ENTRY", 
		joinColumns = {@JoinColumn(name="parent_id" ,referencedColumnName="id")},
		inverseJoinColumns = {@JoinColumn(name="child_id", referencedColumnName="id")})
	public Set<GroupableDbo> childreen;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<GroupableDbo> getChildreen() {
		return childreen;
	}

	public void setChildreen(Set<GroupableDbo> childreen) {
		this.childreen = childreen;
	}
}
