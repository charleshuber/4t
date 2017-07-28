package fr.metz.surfthevoid.tttt.rest.time.cron;

public class SecondsMinutesParser extends AbstractTimeParser {
	
	public static final String value = "([1-5]?[0-9])"; 
	
	protected SecondsMinutesParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 59;
	}
}
