package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="PERIOD")
public class PeriodDbo extends GenericDbo {
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_TIME", nullable=false)
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_TIME", nullable=false)
	private Date endTime;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
