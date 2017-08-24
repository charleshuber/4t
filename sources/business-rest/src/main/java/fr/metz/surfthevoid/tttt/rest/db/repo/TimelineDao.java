package fr.metz.surfthevoid.tttt.rest.db.repo;

import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.TimelineDbo_;

@Named
public class TimelineDao extends GenericDao<TimelineDbo> {
	
	public TimelineDbo readByName(String name){
		if(StringUtils.isEmpty(name)){
			return null;
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TimelineDbo> cq = cb.createQuery(TimelineDbo.class);
		Root<TimelineDbo> root = cq.from(TimelineDbo.class);
		cq.select(root);
		cq.where(cb.equal(root.get(TimelineDbo_.name), name));
		TypedQuery<TimelineDbo> tq = em.createQuery(cq);
		return getSingleResult(tq);
	}
	
}
