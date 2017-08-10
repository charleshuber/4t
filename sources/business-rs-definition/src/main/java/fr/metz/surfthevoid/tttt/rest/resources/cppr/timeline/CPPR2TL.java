package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

import fr.metz.surfthevoid.tttt.rest.resources.Resource;

public class CPPR2TL extends Resource {
	
	public static final String NEGATIVE_FIELD_NAME = "negative";
	public static final String ORDER_FIELD_NAME = "order";
	public static final String CPPR_ID_FIELD_NAME = "compiledPeriodId";
	public static final String TL_ID_FIELD_NAME = "timelineId";
	
	private Boolean negative;
	private Integer order;
	private Long compiledPeriodId;
	private Long timelineId;
	
	public Boolean getNegative() {
		return negative;
	}
	public void setNegative(Boolean negative) {
		this.negative = negative;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Long getCompiledPeriodId() {
		return compiledPeriodId;
	}
	public void setCompiledPeriodId(Long compiledPeriodId) {
		this.compiledPeriodId = compiledPeriodId;
	}
	public Long getTimelineId() {
		return timelineId;
	}
	public void setTimelineId(Long timelineId) {
		this.timelineId = timelineId;
	}
}
