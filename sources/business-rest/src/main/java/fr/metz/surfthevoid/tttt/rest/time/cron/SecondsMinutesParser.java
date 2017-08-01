package fr.metz.surfthevoid.tttt.rest.time.cron;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TreeSet;

import fr.metz.surfthevoid.tttt.rest.time.cron.AbstractTimeParser.BasicParsingResult;
import fr.metz.surfthevoid.tttt.rest.time.cron.SecondsMinutesParser.SecondsMinutesParsingResult;

public class SecondsMinutesParser extends AbstractTimeParser<SecondsMinutesParsingResult> {
	
	private static final String value = "([1-5]?[0-9])"; 
	
	private static final TreeSet<Integer> allPeriodValues = new TreeSet<>(Arrays.asList(
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14,15,16,17,18,19,
			20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,
			40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59));
	
	
	protected SecondsMinutesParser() {
		super(value);
	}

	@Override
	protected Integer getMaxTimeValue() {
		return allPeriodValues.last();
	}

	@Override
	protected SecondsMinutesParsingResult newDayParsingResult() {
		return new SecondsMinutesParsingResult();
	}
	
	public static class SecondsMinutesParsingResult extends BasicParsingResult{
		
		@Override
		protected TreeSet<Integer> getAllPermittedValues(LocalDateTime dateTime) {
			return allPeriodValues;
		}
		
	}
}
