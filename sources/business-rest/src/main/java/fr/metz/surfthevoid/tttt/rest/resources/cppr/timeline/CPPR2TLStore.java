package fr.metz.surfthevoid.tttt.rest.resources.cppr.timeline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CPPR2TLDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.CPPR2TLDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.CompiledPeriodDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.TimeLineDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

@Named
public class CPPR2TLStore extends ResourceStore<CPPR2TL, CPPR2TLDbo>{
	
	@Inject
	protected CPPR2TLDao dao;
	
	@Inject
	protected CompiledPeriodDao cpprDao;
	
	@Inject
	protected TimeLineDao tlDao;
	
	@Inject
	protected CPPR2TLValidator validator;
	
	public Set<CPPR2TL> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toCollection(getOrderedByOrder()));
	}
	
	public Set<CPPR2TL> allOfCPPR(Long cpprId) throws ValidationException {
		CompiledPeriodDbo dbCppr = cpprDao.read(cpprId);			
		if(dbCppr != null){
			if(CollectionUtils.isNotEmpty(dbCppr.getCp2tls())){
				return dbCppr.getCp2tls().stream()
				.map(dbCPPR2TL -> extract(dbCPPR2TL))
				.collect(Collectors.toCollection(getOrderedByOrder()));
			}
			return new HashSet<CPPR2TL>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	@Override
	protected CPPR2TLDao getDao() {
		return dao;
	}

	@Override
	protected CPPR2TLValidator getValidator() {
		return validator;
	}

	@Override
	protected CPPR2TLDbo transform(CPPR2TL res) {
		CPPR2TLDbo CPPR2TLDbo = new CPPR2TLDbo();
		CPPR2TLDbo.setId(res.getId());
		CPPR2TLDbo.setCmpPeriod(cpprDao.read(res.getCompiledPeriodId()));
		CPPR2TLDbo.setTimeline(tlDao.read(res.getTimelineId()));
		CPPR2TLDbo.setNegative(res.getNegative());
		updateOrders(CPPR2TLDbo, res.getOrder());
		return CPPR2TLDbo;
	}
	
	
	protected void updateOrders(CPPR2TLDbo cppr2TLDbo, Integer order){
		List<CPPR2TLDbo> dbCollection = cppr2TLDbo.getCmpPeriod().getCp2tls();
		//If there is no other link we set it as the first one and we leave
		if(CollectionUtils.isEmpty(dbCollection)){
			cppr2TLDbo.setOrder(1);
			return;
		}
		if(order == null || order.intValue() > dbCollection.size()){
			order = dbCollection.size() + 1;
		} else if(order.intValue() < 0){
			cppr2TLDbo.setOrder(1);
		}
		cppr2TLDbo.setOrder(order);
		updateOrders(cppr2TLDbo);
	}
	
	
	protected void updateOrders(CPPR2TLDbo cppr2TLDbo){
		ArrayList<CPPR2TLDbo> tmpOrderedCollection = new ArrayList<>(cppr2TLDbo.getCmpPeriod().getCp2tls());
		//we remove the updated element from the temporary list
		if(cppr2TLDbo.getId() != null){
			tmpOrderedCollection = tmpOrderedCollection.stream()
			.filter(link -> !link.getId().equals(cppr2TLDbo.getId()))
			.collect(Collectors.toCollection(() -> new ArrayList<>()));
		}
		tmpOrderedCollection.add(cppr2TLDbo.getOrder().intValue() - 1, cppr2TLDbo);
		tmpOrderedCollection.sort(Comparator.comparing(CPPR2TLDbo::getOrder));
		for(int i=0; i < tmpOrderedCollection.size(); i++){
			tmpOrderedCollection.get(i).setOrder(i+1);
		}
	}

	@Override
	public CPPR2TL extract(CPPR2TLDbo dbo) {
		CPPR2TL CPPR2TL = new CPPR2TL();
		CPPR2TL.setId(dbo.getId());
		CPPR2TL.setCompiledPeriodId(dbo.getCmpPeriod().getId());
		CPPR2TL.setTimelineId(dbo.getTimeline().getId());
		CPPR2TL.setNegative(dbo.getNegative());
		CPPR2TL.setOrder(dbo.getOrder());
		return CPPR2TL;
	}

	@Override
	protected CPPR2TL fillWithDefault(CPPR2TL res, Operation op) {
		if(res.getNegative() == null){
			res.setNegative(false);
		}
		return res;
	}
	
	public Supplier<TreeSet<CPPR2TL>> getOrderedByOrder(){
		return () -> new TreeSet<CPPR2TL>(Comparator.comparing(CPPR2TL::getOrder));
	}
	
}
