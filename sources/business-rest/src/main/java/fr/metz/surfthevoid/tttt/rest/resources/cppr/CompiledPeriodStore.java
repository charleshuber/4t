package fr.metz.surfthevoid.tttt.rest.resources.cppr;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimeLineDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimeLineDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

@Named
public class CompiledPeriodStore extends ResourceStore<CompiledPeriod, CompiledPeriodDbo>{
	
	@Inject
	protected CompiledPeriodDao dao;
	
	@Inject
	protected TimeLineDao tlDao;
	
	@Inject
	protected CompiledPeriodValidator validator;
	
	public Set<CompiledPeriod> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toCollection(getOrderedByName()));
	}
	
	public Set<CompiledPeriod> allOfTimeline(Long tlid) throws ValidationException {
		TimeLineDbo dbTimeLine = tlDao.read(tlid);			
		if(dbTimeLine != null){
			if(CollectionUtils.isNotEmpty(dbTimeLine.getCompPeriods())){
				return dbTimeLine.getCompPeriods().stream()
				.map(dbCPPR -> extract(dbCPPR))
				.collect(Collectors.toCollection(getOrderedById()));
			}
			return new HashSet<CompiledPeriod>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	@Override
	protected CompiledPeriodDao getDao() {
		return dao;
	}

	@Override
	protected CompiledPeriodValidator getValidator() {
		return validator;
	}

	@Override
	protected CompiledPeriodDbo transform(CompiledPeriod res) {
		CompiledPeriodDbo CompiledPeriodDbo = new CompiledPeriodDbo();
		CompiledPeriodDbo.setId(res.getId());
		CompiledPeriodDbo.setName(res.getName());
		return CompiledPeriodDbo;
	}

	@Override
	public CompiledPeriod extract(CompiledPeriodDbo dbo) {
		CompiledPeriod CompiledPeriod = new CompiledPeriod();
		CompiledPeriod.setId(dbo.getId());
		CompiledPeriod.setName(dbo.getName());
		return CompiledPeriod;
	}

	@Override
	protected CompiledPeriod clean(CompiledPeriod res, Operation op) {
		if(res != null){
			if(StringUtils.isNotEmpty(res.getName())){
				res.setName(res.getName().trim());
			}
		}
		return res;
	}
	
	public Supplier<TreeSet<CompiledPeriod>> getOrderedByName(){
		return () -> new TreeSet<CompiledPeriod>(Comparator.comparing(CompiledPeriod::getName));
	}
	
	public Supplier<TreeSet<CompiledPeriod>> getOrderedById(){
		return () -> new TreeSet<CompiledPeriod>(Comparator.comparing(CompiledPeriod::getId));
	}
}
