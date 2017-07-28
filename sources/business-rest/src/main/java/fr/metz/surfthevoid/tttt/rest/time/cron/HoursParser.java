package fr.metz.surfthevoid.tttt.rest.time.cron;

public class HoursParser extends AbstractTimeParser {
	
	public static final String value = "([0-9]|1[0-9]|2[0-3])";
	
	protected HoursParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 23;
	}
}
