package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="TIMELINE")
public class TimeLineDbo extends GenericDbo {
	
	@Column(name="NEG", unique=true, nullable=false)
	private boolean negative; 
	
	@ManyToMany
	@JoinTable(name="TL_2_PR", 
		joinColumns = @JoinColumn(name="TL_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="PR_ID", referencedColumnName="ID"))
	private Set<PeriodDbo> periods;
	
	@ManyToMany
	@JoinTable(name="TL_2_CP", 
		joinColumns = @JoinColumn(name="TL_ID" ,referencedColumnName="ID"),
		inverseJoinColumns = @JoinColumn(name="CP_ID", referencedColumnName="ID"))
	private Set<CronPeriodDbo> cronPeriods;

	public boolean isNegative() {
		return negative;
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
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
}
