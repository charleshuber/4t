package fr.metz.surfthevoid.tttt.rest.db.repo;

import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.GroupDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.GroupDbo_;

@Named
public class GroupDao extends GenericDao<GroupDbo> {
	
	public GroupDbo readByName(String name){
		if(StringUtils.isEmpty(name)){
			return null;
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GroupDbo> cq = cb.createQuery(GroupDbo.class);
		Root<GroupDbo> root = cq.from(GroupDbo.class);
		cq.select(root);
		cq.where(cb.equal(root.get(GroupDbo_.name), name));
		TypedQuery<GroupDbo> tq = em.createQuery(cq);
		return getSingleResult(tq);
	}
}
