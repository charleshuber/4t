package fr.metz.surfthevoid.tttt.rest.db.repo;

import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo_;

@Named
public class UserDao extends GenericDao<UserDbo> {
	
	public UserDbo readByMail(String email){
		if(StringUtils.isEmpty(email)){
			return null;
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserDbo> cq = cb.createQuery(UserDbo.class);
		Root<UserDbo> root = cq.from(UserDbo.class);
		cq.select(root);
		cq.where(cb.equal(root.get(UserDbo_.email), email));
		TypedQuery<UserDbo> tq = em.createQuery(cq);
		return getSingleResult(tq);
	}
}
