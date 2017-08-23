package fr.metz.surfthevoid.tttt.rest.resources.cronperiod;

import java.time.Duration;
import java.time.Period;

import javax.inject.Inject;
import javax.inject.Named;

import fr.metz.surfthevoid.tttt.rest.db.entity.CronPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CronPeriodDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;
import fr.metz.surfthevoid.tttt.rest.time.cron.CronExpressionAnalyser;

@Named
public class CronPeriodValidator extends Validator<CronPeriod, CronPeriodDbo>{
	
	@Inject
	protected CronPeriodDao dao;
	
	@Override
	public void validateInput(CronPeriod input, Operation op, Errors errors) throws ValidationException {
		try{
			new CronExpressionAnalyser(input.toCronExpression());
		} catch(Exception e){
			errors.addGlobalError(CronPeriodValidationErrors.CRON_IS_INVALID.getCode());
		}
		Duration duration = input.toDuration();
		Period period = input.toPeriod();
		if(duration == Duration.ZERO && period == Period.ZERO){
			errors.addGlobalError(CronPeriodValidationErrors.PERIOD_IS_INVALID.getCode());
		}
	}

	@Override
	protected CronPeriodDao getDao() {
		return dao;
	}
	
}
