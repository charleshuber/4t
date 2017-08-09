package fr.metz.surfthevoid.tttt.rest.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="CRON_PERIOD")
public class CronPeriodDbo extends GenericDbo {
	
	// CRON expression for seconds
	@Column(name="SC_EXP", nullable=false)
	private String scExp;
	
	// CRON expression for minutes
	@Column(name="MN_EXP", nullable=false)
	private String mnExp;
	
	// CRON expression for hours
	@Column(name="HR_EXP", nullable=false)
	private String hrExp;
	
	// CRON expression for days of month
	@Column(name="DM_EXP", nullable=false)
	private String dmExp;
	
	// CRON expression for months
	@Column(name="MT_EXP", nullable=false)
	private String mtExp;
	
	// CRON expression for days of week
	@Column(name="DW_EXP", nullable=false)
	private String dwExp;
	
	// CRON expression for year
	@Column(name="YR_EXP", nullable=true)
	private String yrExp;
	
	// number of seconds for the period
	@Column(name="SC_DUR", nullable=true)
	private String scDur;
	
	// number of minutes for the period
	@Column(name="MN_DUR", nullable=true)
	private String mnDur;
	
	// number of hours for the period
	@Column(name="HR_DUR", nullable=true)
	private String hrDur;
	
	// number of days for the period
	@Column(name="DY_DUR", nullable=true)
	private String dyDur;
	
	// number of months for the period
	@Column(name="MT_DUR", nullable=true)
	private String mtDur;
	
	// number of years for the period
	@Column(name="YR_DUR", nullable=true)
	private String yrDur;

	public String getScExp() {
		return scExp;
	}

	public void setScExp(String scExp) {
		this.scExp = scExp;
	}

	public String getMnExp() {
		return mnExp;
	}

	public void setMnExp(String mnExp) {
		this.mnExp = mnExp;
	}

	public String getHrExp() {
		return hrExp;
	}

	public void setHrExp(String hrExp) {
		this.hrExp = hrExp;
	}

	public String getDmExp() {
		return dmExp;
	}

	public void setDmExp(String dmExp) {
		this.dmExp = dmExp;
	}

	public String getMtExp() {
		return mtExp;
	}

	public void setMtExp(String mtExp) {
		this.mtExp = mtExp;
	}

	public String getDwExp() {
		return dwExp;
	}

	public void setDwExp(String dwExp) {
		this.dwExp = dwExp;
	}

	public String getYrExp() {
		return yrExp;
	}

	public void setYrExp(String yrExp) {
		this.yrExp = yrExp;
	}

	public String getScDur() {
		return scDur;
	}

	public void setScDur(String scDur) {
		this.scDur = scDur;
	}

	public String getMnDur() {
		return mnDur;
	}

	public void setMnDur(String mnDur) {
		this.mnDur = mnDur;
	}

	public String getHrDur() {
		return hrDur;
	}

	public void setHrDur(String hrDur) {
		this.hrDur = hrDur;
	}

	public String getDyDur() {
		return dyDur;
	}

	public void setDyDur(String dyDur) {
		this.dyDur = dyDur;
	}

	public String getMtDur() {
		return mtDur;
	}

	public void setMtDur(String mtDur) {
		this.mtDur = mtDur;
	}

	public String getYrDur() {
		return yrDur;
	}

	public void setYrDur(String yrDur) {
		this.yrDur = yrDur;
	}
}
