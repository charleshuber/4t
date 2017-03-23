package fr.metz.surfthevoid.tttt.rest.db.repo;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;

@Transactional(TxType.REQUIRED)
public abstract class GenericDao<T extends GenericDbo> {
	
	protected Class<T> type;
	
	@PersistenceContext
	protected EntityManager em;
	
	@SuppressWarnings("unchecked")
	public GenericDao(){
		Class<?> implementationClass = this.getClass();
		ParameterizedType currentAbstractClass = (ParameterizedType) implementationClass.getGenericSuperclass();
		type  = (Class<T>) currentAbstractClass.getActualTypeArguments()[0];
	}
	
	public void create(T dbo){
		em.persist(dbo);
	}
	
	public T update(T dbo){
		return em.merge(dbo);
	}
	
	public T read(Long id){
		if(id != null){
			return em.getReference(type, id);
		}
		return null;
	}
	
	public void delete(T dbo){
		em.remove(dbo);
	}
	
	public Set<T> readAll(){
		Set<T> results = new HashSet<>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(type);
		Root<T> table = cq.from(type);
		cq.select(table);
		results.addAll(em.createQuery(cq).getResultList());
		return results;
	}
} 
