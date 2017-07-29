package fr.metz.surfthevoid.tttt.rest.time.cron;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;

public class MonthsParser extends AbstractTimeParser<BasicParsingResult> {
	
	public static final String value = "([1-9]|1[0-2])";
	
	protected MonthsParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 12;
	}
	
	@Override
	protected BasicParsingResult newDayParsingResult() {
		return new BasicParsingResult();
	}
}
