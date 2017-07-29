package fr.metz.surfthevoid.tttt.rest.time.cron;

public class MonthsParser extends AbstractTimeParser {
	
	public static final String value = "([1-9]|1[0-2])";
	
	protected MonthsParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 12;
	}
}
