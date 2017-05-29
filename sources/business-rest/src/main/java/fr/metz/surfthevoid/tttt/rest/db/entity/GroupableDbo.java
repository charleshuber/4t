package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class GroupableDbo extends GenericDbo {
	
	@ManyToMany(mappedBy="childreen")
	public Set<GroupDbo> parents;

	public Set<GroupDbo> getParents() {
		return parents;
	}

	public void setParents(Set<GroupDbo> parents) {
		this.parents = parents;
	}
}
