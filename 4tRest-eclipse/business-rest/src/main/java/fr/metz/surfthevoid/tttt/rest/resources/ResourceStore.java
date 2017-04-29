package fr.metz.surfthevoid.tttt.rest.resources;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GenericDao;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;


@Transactional(TxType.REQUIRES_NEW)
public abstract class ResourceStore<R extends Resource, T extends GenericDbo> {
	
	protected abstract GenericDao<T> getDao(); 
	protected abstract Validator<R,T> getValidator();
	
	public R create(R res) throws ValidationException{
		T dbo = validateAndTransform(res, Operation.CREATE);
		getDao().create(dbo);
		return extract(dbo);
	}
	
	public R read(Long id) throws ValidationException{
		getValidator().validateId(id);
		T dboToRead = getDao().read(id);
		if(dboToRead == null){
			throw new ValidationException(Type.NO_CONTENT, new Errors());
		}
		return extract(dboToRead);
	}
	
	public R update(R res) throws ValidationException{
		T dbo = validateAndTransform(res, Operation.UDPDATE);
		T mergedDbo = getDao().update(dbo);
		return extract(mergedDbo);
	}

	public R delete(Long id) throws ValidationException{
		getValidator().validateId(id);
		T dboToDelete = getDao().read(id);
		if(dboToDelete == null){
			throw new ValidationException(Type.NO_CONTENT, new Errors());
		}
		getDao().delete(dboToDelete);
		return extract(dboToDelete);
	}
	
	protected T validateAndTransform(R res, Operation op) throws ValidationException{
		getValidator().validate(clean(res), op);
		return transform(res);
	}
	
	protected R clean(R res) { return res; };
	protected abstract T transform(R res);
	protected abstract R extract(T dbo);
}