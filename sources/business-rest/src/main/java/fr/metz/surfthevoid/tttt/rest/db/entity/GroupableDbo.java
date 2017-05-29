package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="GRP_ABLE")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TYPE")
public abstract class GroupableDbo extends GenericDbo {
	
	@ManyToMany(mappedBy="childreen")
	public Set<GroupDbo> parents;

	public Set<GroupDbo> getParents() {
		return parents;
	}

	public void setParents(Set<GroupDbo> parents) {
		this.parents = parents;
	}
}
