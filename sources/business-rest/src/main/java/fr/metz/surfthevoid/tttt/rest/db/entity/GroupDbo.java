package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;

@Entity
@Table(name="USR")
public class GroupDbo extends GenericDbo {
	
	@Column(name="NAME", unique=true, nullable=false)
	public String name;
	
	@ManyToMany
	@JoinTable(name="GRP_2_USR", 
		joinColumns = @JoinColumn(name="GROUP_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="USER_ID", referencedColumnName="ID"))
	public Set<UserDbo> users;
	
	@ManyToMany
	@JoinTable(name="GRP_2_GRP", 
		joinColumns = @JoinColumn(name="PARENT_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="CHILD_ID", referencedColumnName="ID"))
	public Set<GroupDbo> children;
	
	@ManyToMany(mappedBy="children")
	public Set<GroupDbo> parents;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UserDbo> getUsers() {
		return users;
	}

	public void setUsers(Set<UserDbo> users) {
		this.users = users;
	}

	public Set<GroupDbo> getChildren() {
		return children;
	}

	public void setChildren(Set<GroupDbo> children) {
		this.children = children;
	}

	public Set<GroupDbo> getParents() {
		return parents;
	}

	public void setParents(Set<GroupDbo> parents) {
		this.parents = parents;
	}
	
	public Boolean hasChildGroup(Long childId){
		if(childId != null && CollectionUtils.isNotEmpty(children)){
			return children.stream().anyMatch((child) -> childId.equals(child.getId()) || child.hasChildGroup(childId));
		}
		return false;
	}
}
