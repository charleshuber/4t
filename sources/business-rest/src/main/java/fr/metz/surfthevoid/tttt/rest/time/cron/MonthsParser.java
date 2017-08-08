package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TreeSet;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.MonthsParser.MonthsParsingResult;

public class MonthsParser extends AbstractTimeParser<MonthsParsingResult> {
	
	//Be care full to put the single unit value at the end to avoid shortcut
	private static final String value = "(1[0-2]|[1-9])";
	
	private static final TreeSet<Integer> allPeriodValues = new TreeSet<Integer>(Arrays.asList(
			1,2,3,4,5,6,7,8,9,10,11,12));
	
	protected MonthsParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return allPeriodValues.last();
	}
	
	@Override
	protected MonthsParsingResult newParsingResult() {
		return new MonthsParsingResult();
	}
	
	public static class MonthsParsingResult extends BasicParsingResult {
		
		@Override
		protected TreeSet<Integer> getAllPermittedValues(LocalDateTime dateTime) {
			return allPeriodValues;
		}
		
	}
	
}
