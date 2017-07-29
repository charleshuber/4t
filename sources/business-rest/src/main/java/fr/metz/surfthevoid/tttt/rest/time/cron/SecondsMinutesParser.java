package fr.metz.surfthevoid.tttt.rest.time.cron;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;

public class SecondsMinutesParser extends AbstractTimeParser<BasicParsingResult> {
	
	public static final String value = "([1-5]?[0-9])"; 
	
	protected SecondsMinutesParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return 59;
	}

	@Override
	protected BasicParsingResult newDayParsingResult() {
		return new BasicParsingResult();
	}
}
