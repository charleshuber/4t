package fr.metz.surfthevoid.tttt.rest.time;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.Boundary;
import fr.metz.surfthevoid.tttt.rest.resources.time.ITimeCompilatorBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.time.TimeInterval;

@Named("timeCompilatorBoundary")
public class TimeCompilatorBoundary extends Boundary implements ITimeCompilatorBoundary{
	
	@Inject
	protected TimeCompilator timeCompilator;

	@Override
	public Response timelineCompilation(Long tlid, Date start, Date end) {
		ReadSetInterface<TimeInterval> readSetAction = () -> timeCompilator.timelineCompilation(tlid, start, end);
		return readSet(readSetAction);
	}

	@Override
	public Response cpprCompilation(Long cpprid, Date start, Date end) {
		ReadSetInterface<TimeInterval> readSetAction = () -> timeCompilator.cpprCompilation(cpprid, start, end);
		return readSet(readSetAction);
	}
}
