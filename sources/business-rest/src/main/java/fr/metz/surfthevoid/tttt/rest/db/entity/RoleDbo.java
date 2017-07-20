package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="ROLE")
public class RoleDbo extends GenericDbo {
	
	@Column(name="NAME", unique=true, nullable=false)
	@Enumerated(EnumType.STRING)
	public Role name;
	
	@ManyToMany
	@JoinTable(name="ROLE_2_USR", 
		joinColumns = @JoinColumn(name="ROLE_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="USER_ID", referencedColumnName="ID"))
	public Set<UserDbo> users;
	
	@ManyToMany
	@JoinTable(name="ROLE_2_GRP", 
		joinColumns = @JoinColumn(name="ROLE_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="GRP_ID", referencedColumnName="ID"))
	public Set<GroupDbo> groups;
	

	public Role getName() {
		return name;
	}

	public void setName(Role name) {
		this.name = name;
	}

	public Set<UserDbo> getUsers() {
		return users;
	}

	public void setUsers(Set<UserDbo> users) {
		this.users = users;
	}
	
	public static enum Role {
		ROOT,
		ADMIN,
		USER
	}
}
