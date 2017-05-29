package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="GRP")
@DiscriminatorValue("G")
public class GroupDbo extends GroupableDbo {
	
	@Column(name="NAME", unique=true, nullable=false)
	public String name;
	
	@ManyToMany
	@JoinTable(name="GRP_ENTRY", 
		joinColumns = @JoinColumn(name="PARENT_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="CHILD_ID", referencedColumnName="ID"))
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
