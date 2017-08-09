package fr.metz.surfthevoid.tttt.rest.db.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="COMP_PERIOD")
public class CompiledPeriodDbo extends GenericDbo {
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_TIME", nullable=false)
	private LocalDateTime startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_TIME", nullable=false)
	private LocalDateTime endTime;

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
}
