package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="COMP_PERIOD")
public class CompiledPeriodDbo extends GenericDbo {
		
	@Column(name="NAME", unique=true, nullable=false)
	private String name;
	
	@OneToMany(mappedBy="cmpPeriod")
	private List<CPPR2TLDbo> cp2tls;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CPPR2TLDbo> getCp2tls() {
		return cp2tls;
	}

	public void setCp2tls(List<CPPR2TLDbo> cp2tls) {
		this.cp2tls = cp2tls;
	}
}
