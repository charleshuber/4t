package fr.metz.surfthevoid.tttt.rest.time.cron;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;

public class HoursParser extends AbstractTimeParser<BasicParsingResult> {
	
	public static final String value = "([0-9]|1[0-9]|2[0-3])";
	
	protected HoursParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 23;
	}
	
	@Override
	protected BasicParsingResult newDayParsingResult() {
		return new BasicParsingResult();
	}
}
