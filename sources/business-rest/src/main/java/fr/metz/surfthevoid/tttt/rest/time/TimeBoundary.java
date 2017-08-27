package fr.metz.surfthevoid.tttt.rest.time;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.Boundary;
import fr.metz.surfthevoid.tttt.rest.resources.time.ITimeBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.time.TimeInterval;

@Named("timeBoundary")
public class TimeBoundary extends Boundary implements ITimeBoundary{
	
	@Inject
	protected TimeManager timeManager;

	@Override
	public Response timelineCompilation(Long tlid, Date start, Date end) {
		ReadSetInterface<TimeInterval> readSetAction = () -> timeManager.timelineCompilation(tlid, start, end);
		return readSet(readSetAction);
	}

	@Override
	public Response cpprCompilation(Long cpprid, Date start, Date end) {
		ReadSetInterface<TimeInterval> readSetAction = () -> timeManager.cpprCompilation(cpprid, start, end);
		return readSet(readSetAction);
	}
}
