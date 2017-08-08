package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TreeSet;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.HoursParser.HoursParsingResult;

public class HoursParser extends AbstractTimeParser<HoursParsingResult> {
	
	//Be care full to put the single unit value at the end to avoid shortcut
	private static final String value = "(1[0-9]|2[0-3]|[0-9])";
	
	private static final TreeSet<Integer> allPeriodValues = new TreeSet<>(Arrays.asList(
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
			10,11,12,13,14,15,16,17,18,19, 
			20,21,22,23));
	
	protected HoursParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return allPeriodValues.last();
	}
	
	@Override
	protected HoursParsingResult newParsingResult() {
		return new HoursParsingResult();
	}
	
	public static class HoursParsingResult extends BasicParsingResult {
		@Override
		protected TreeSet<Integer> getAllPermittedValues(LocalDateTime dateTime) {
			return allPeriodValues;
		}
		
	}
}
