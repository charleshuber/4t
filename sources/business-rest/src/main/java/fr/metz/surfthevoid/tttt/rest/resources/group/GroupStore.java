package fr.metz.surfthevoid.tttt.rest.resources.group;

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

import fr.metz.surfthevoid.tttt.rest.db.entity.GroupDbo;
import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GroupDao;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;
import fr.metz.surfthevoid.tttt.rest.resources.Operation;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceStore;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;
import fr.metz.surfthevoid.tttt.rest.resources.user.User;
import fr.metz.surfthevoid.tttt.rest.resources.user.UserStore;

@Named
public class GroupStore extends ResourceStore<Group, GroupDbo>{
	
	@Inject
	protected GroupDao dao;
	
	@Inject
	protected UserDao userDao;
	
	@Inject
	protected UserStore userStore;
	
	@Inject
	protected GroupValidator validator;
	
	public Set<Group> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toCollection(getOrderedByName()));
	}
	
	public Set<Group> children(Long id) throws ValidationException {
		GroupDbo dbGroup = dao.read(id);			
		if(dbGroup != null){
			if(CollectionUtils.isNotEmpty(dbGroup.getChildren())){
				return dbGroup.getChildren().stream()
				.map(dbChild -> extract(dbChild))
				.collect(Collectors.toCollection(getOrderedByName()));
			}
			return new HashSet<Group>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	public Set<Group> parents(Long id) throws ValidationException {
		GroupDbo dbGroup = dao.read(id);			
		if(dbGroup != null){
			if(CollectionUtils.isNotEmpty(dbGroup.getParents())){
				return dbGroup.getParents().stream()
				.map(dbChild -> extract(dbChild))
				.collect(Collectors.toCollection(getOrderedByName()));
			}
			return new HashSet<Group>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	public Set<User> users(Long id) throws ValidationException {
		GroupDbo dbGroup = dao.read(id);			
		if(dbGroup != null){
			if(CollectionUtils.isNotEmpty(dbGroup.getUsers())){
				return dbGroup.getUsers().stream()
				.map(dbUser -> userStore.extract(dbUser))
				.collect(Collectors.toCollection(userStore.getOrderedById()));
			}
			return new HashSet<User>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	public Boolean addChild(Long groupId, Long childGroupId) throws ValidationException {
		GroupDbo dbGroup = dao.read(groupId);
		GroupDbo dbChildGroup = dao.read(childGroupId);
		if(dbGroup != null && dbChildGroup != null && !groupId.equals(childGroupId)){
			if(dbGroup.hasChildGroup(childGroupId)){
				Errors errors = new Errors();
				errors.addGlobalError("The group with id " + childGroupId + " is already a child of the group with id " + groupId);
				throw new ValidationException(Type.CONFLICT, errors);
			}
			if(dbChildGroup.hasChildGroup(groupId)){
				Errors errors = new Errors();
				errors.addGlobalError("Cyclic dependency: The group with id " + childGroupId + " is a parent of the group with id " + groupId);
				throw new ValidationException(Type.CONFLICT, errors);
			}
			if(dbGroup.getChildren() == null) dbGroup.setChildren(new HashSet<GroupDbo>());
			dbGroup.getChildren().add(dbChildGroup);
			return true;
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	public Boolean removeChild(Long groupId, Long childGroupId) throws ValidationException {
		GroupDbo dbGroup = dao.read(groupId);
		GroupDbo dbChildGroup = dao.read(childGroupId);
		if(dbGroup != null && dbChildGroup != null && !groupId.equals(childGroupId)){
			if(CollectionUtils.isNotEmpty(dbGroup.getChildren()) 
					&& dbGroup.getChildren().contains(dbChildGroup)){
				dbGroup.getChildren().remove(dbChildGroup);
				return true;
			}
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	public Boolean addUser(Long groupId, Long userId) throws ValidationException {
		GroupDbo dbGroup = dao.read(groupId);
		UserDbo dbUser = userDao.read(userId);
		if(dbGroup != null && dbUser != null){
			if(CollectionUtils.isNotEmpty(dbGroup.getUsers()) 
					&& dbGroup.getUsers().contains(dbUser)){
				Errors errors = new Errors();
				errors.addGlobalError("The user with id " + userId + " is already a child of the group with id " + groupId);
				throw new ValidationException(Type.CONFLICT, errors);
			}
			if(dbGroup.getUsers() == null) dbGroup.setUsers(new HashSet<UserDbo>());
			dbGroup.getUsers().add(dbUser);
			return true;
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	public Boolean removeUser(Long groupId, Long userId) throws ValidationException {
		GroupDbo dbGroup = dao.read(groupId);
		UserDbo dbUser = userDao.read(userId);
		if(dbGroup != null && dbUser != null){
			if(CollectionUtils.isNotEmpty(dbGroup.getUsers()) 
					&& dbGroup.getUsers().contains(dbUser)){
				dbGroup.getUsers().remove(dbUser);
				return true;
			}
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
	}
	
	@Override
	protected GroupDao getDao() {
		return dao;
	}

	@Override
	protected GroupValidator getValidator() {
		return validator;
	}

	@Override
	protected GroupDbo transform(Group res) {
		GroupDbo GroupDbo = new GroupDbo();
		GroupDbo.setId(res.getId());
		GroupDbo.setName(res.getName());
		return GroupDbo;
	}

	@Override
	public Group extract(GroupDbo dbo) {
		Group Group = new Group();
		Group.setId(dbo.getId());
		Group.setName(dbo.getName());
		return Group;
	}

	@Override
	protected Group clean(Group res, Operation op) {
		if(StringUtils.isNotEmpty(res.getName())){
			res.setName(res.getName().trim());
		}
		return res;
	}
	
	public Supplier<TreeSet<Group>> getOrderedByName(){
		return () -> new TreeSet<Group>(Comparator.comparing(Group::getName));
	}
}
