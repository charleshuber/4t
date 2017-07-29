package fr.metz.surfthevoid.tttt.rest.time.cron;

public class YearsParser extends AbstractTimeParser {
	
	public static final String value = "(\\d+)";
	
	protected YearsParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 12;
	}
}
