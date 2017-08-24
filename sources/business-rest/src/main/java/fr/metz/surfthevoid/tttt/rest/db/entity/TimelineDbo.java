package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TYPE")
@Table(name="TIMELINE")
public class TimelineDbo extends GenericDbo {
	
	@Column(name="NAME", unique=true, nullable=false)
	private String name;
	
	@ManyToMany
	@JoinTable(name="TL_2_PR", 
		joinColumns = @JoinColumn(name="TL_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="PR_ID", referencedColumnName="ID"))
	private Set<PeriodDbo> periods;
	
	@ManyToMany
	@JoinTable(name="TL_2_CRPR", 
		joinColumns = @JoinColumn(name="TL_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="CRPR_ID", referencedColumnName="ID"))
	private Set<CronPeriodDbo> cronPeriods;
	
	@ManyToMany
	@JoinTable(name="TL_2_CPPR", 
		joinColumns = @JoinColumn(name="TL_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="CPPR_ID", referencedColumnName="ID"))
	//TODO Warn to cyclic dependencies
	private Set<CompiledPeriodDbo> compPeriods;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<PeriodDbo> getPeriods() {
		return periods;
	}

	public void setPeriods(Set<PeriodDbo> periods) {
		this.periods = periods;
	}

	public Set<CronPeriodDbo> getCronPeriods() {
		return cronPeriods;
	}

	public void setCronPeriods(Set<CronPeriodDbo> cronPeriods) {
		this.cronPeriods = cronPeriods;
	}

	public Set<CompiledPeriodDbo> getCompPeriods() {
		return compPeriods;
	}

	public void setCompPeriods(Set<CompiledPeriodDbo> compPeriods) {
		this.compPeriods = compPeriods;
	}
}
