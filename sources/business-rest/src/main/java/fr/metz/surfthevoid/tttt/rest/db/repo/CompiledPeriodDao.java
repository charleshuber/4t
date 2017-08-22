package fr.metz.surfthevoid.tttt.rest.db.repo;

import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.CompiledPeriodDbo_;

@Named
public class CompiledPeriodDao extends GenericDao<CompiledPeriodDbo> {
	
	public CompiledPeriodDbo readByName(String name){
		if(StringUtils.isEmpty(name)){
			return null;
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CompiledPeriodDbo> cq = cb.createQuery(CompiledPeriodDbo.class);
		Root<CompiledPeriodDbo> root = cq.from(CompiledPeriodDbo.class);
		cq.select(root);
		cq.where(cb.equal(root.get(CompiledPeriodDbo_.name), name));
		TypedQuery<CompiledPeriodDbo> tq = em.createQuery(cq);
		return getSingleResult(tq);
	}
}
