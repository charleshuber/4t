package fr.metz.surfthevoid.tttt.rest.resources.group;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.GroupDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GroupDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;
import fr.metz.surfthevoid.tttt.rest.resources.Validator;

@Named
public class GroupValidator extends Validator<Group, GroupDbo>{
	
	@Inject
	protected GroupDao dao;
	@Inject
	protected UserDao userDao;
	
	@Override
	public void validateInput(Group input, Operation op, Errors errors) throws ValidationException {
		validatename(input, op, errors);	
	}

	protected void validatename(Group input, Operation op, Errors errors) {
		if(StringUtils.isEmpty(input.getName())){
			errors.addFieldError(Group.NAME_FIELD_NAME, GroupValidationErrors.NAME_IS_MANDATORY.getCode());
		} else {
			GroupDbo anyGroup = dao.readByName(input.getName());
			if(op == Operation.CREATE && anyGroup != null){
				errors.addFieldError(Group.NAME_FIELD_NAME, GroupValidationErrors.NAME_IS_ALREADY_USED.getCode());
			} else if(op == Operation.UDPDATE && (anyGroup != null && anyGroup.getId() != input.getId())){
				errors.addFieldError(Group.NAME_FIELD_NAME, GroupValidationErrors.NAME_IS_ALREADY_USED.getCode());
			}	
		}
	}
	
	public void validateChildAddition(Long groupId, Long childGroupId) throws ValidationException{
		GroupDbo dbGroup = dao.read(groupId);
		GroupDbo dbChildGroup = dao.read(childGroupId);
		if(dbGroup == null || dbChildGroup == null || groupId.equals(childGroupId)){
			throw new ValidationException(Type.BAD_REQUEST, null);
		}
		if(dbGroup.hasChildGroup(childGroupId) 
				|| dbChildGroup.hasChildGroup(groupId)){
			Errors errors = new Errors();
			errors.addGlobalError(GroupValidationErrors.CYCLIC_DEPENDENCY.getCode());
			throw new ValidationException(Type.INVALID_INPUT, errors);
		}
		
	}
	
	public void validateChildDeletion(Long groupId, Long childGroupId) throws ValidationException{
		GroupDbo dbGroup = dao.read(groupId);
		GroupDbo dbChildGroup = dao.read(childGroupId);
		if(dbGroup == null || dbChildGroup == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		}
		else if(CollectionUtils.isEmpty(dbGroup.getChildren()) 
				|| !dbGroup.getChildren().contains(dbChildGroup)){
			throw new ValidationException(Type.NO_CONTENT, null);
		}
	}
	
	public void validateUserAddition(Long groupId, Long userId) throws ValidationException{
		GroupDbo dbGroup = dao.read(groupId);
		UserDbo dbUser = userDao.read(userId);
		if(dbGroup == null || dbUser == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		} 
		else if(CollectionUtils.isNotEmpty(dbGroup.getUsers()) 
					&& dbGroup.getUsers().contains(dbUser)){
			throw new ValidationException(Type.CONFLICT, null);
			
		}
	}
	
	public void validateUserDeletion(Long groupId, Long userId) throws ValidationException{
		GroupDbo dbGroup = dao.read(groupId);
		UserDbo dbUser = userDao.read(userId);
		if(dbGroup == null || dbUser == null){
			throw new ValidationException(Type.BAD_REQUEST, null);
		}
		if(CollectionUtils.isEmpty(dbGroup.getUsers()) 
				|| !dbGroup.getUsers().contains(dbUser)){
			throw new ValidationException(Type.NO_CONTENT, null);
		}
	}

	@Override
	protected GroupDao getDao() {
		return dao;
	}
	
}
