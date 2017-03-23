package fr.metz.surfthevoid.tttt.rest.resources;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GenericDao;

@Transactional(TxType.REQUIRED)
public abstract class Validator<R extends Resource, T extends GenericDbo> {
	
	public void validate(R input, Operation op) throws ValidationException {
		if(input == null){
			throw new ValidationException(Type.INVALID_INPUT);
		}
		validateState(input, op);
		validateInput(input, op);
	}
	
	protected void validateState(R input, Operation op) throws ValidationException {
		if(op == Operation.CREATE && input.getId() != null){
			throw new ValidationException(Type.INVALID_STATE);
		} else if(op == Operation.UDPDATE){ 
			if(input.getId() == null){
				throw new ValidationException(Type.INVALID_STATE);
			} 
			GenericDbo dbo = getDao().read(input.getId());
			if(dbo == null){
				throw new ValidationException(Type.INVALID_STATE);
			}
		}
	}

	protected abstract void validateInput(R input, Operation op) throws ValidationException;
	protected abstract GenericDao<T> getDao();
	
	public static class ValidationException extends Exception {
		private static final long serialVersionUID = 6296828042739307519L;
		private Type type;
		protected ValidationException(Type type){
			this.type = type;
		}
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
		}
	}
	
	public void validateId(Long id) throws ValidationException {
		if(id == null || getDao().read(id) == null){
			throw new ValidationException(Type.NOT_FOUND);
		}
	}
	
	public static enum Type {
		NOT_FOUND,
		INVALID_INPUT,
		INVALID_STATE,
		INVALID_INPUT_DATA
	}
}
