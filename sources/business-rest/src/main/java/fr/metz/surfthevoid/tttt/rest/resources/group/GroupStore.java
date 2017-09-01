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
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

@Named
public class GroupStore extends ResourceStore<Group, GroupDbo>{
	
	@Inject
	protected GroupDao dao;
	@Inject
	protected UserDao userDao;
	@Inject
	protected GroupValidator validator;
	
	public Set<Group> readAll() throws ValidationException {
		return dao.readAll().stream()
				.map(dbo -> extract(dbo))
				.collect(Collectors.toCollection(getOrderedByName()));
	}
	
	public Set<Group> readAllOfUser(Long userId) throws ValidationException {
		UserDbo dbUser =  userDao.read(userId);
		if(dbUser != null){
			if(CollectionUtils.isNotEmpty(dbUser.getGroups())){
				return dbUser.getGroups().stream()
				.map(dbGroup -> extract(dbGroup))
				.collect(Collectors.toCollection(getOrderedByName()));
			}
			return new HashSet<Group>();
		} 
		throw new ValidationException(Type.BAD_REQUEST, null);
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
	
	public void addChild(Long groupId, Long childGroupId) throws ValidationException {
		validator.validateChildAddition(groupId, childGroupId);
		GroupDbo dbGroup = dao.read(groupId);
		GroupDbo dbChildGroup = dao.read(childGroupId);
		dbGroup.getChildren().add(dbChildGroup);
	}
	
	public void removeChild(Long groupId, Long childGroupId) throws ValidationException {
		validator.validateChildDeletion(groupId, childGroupId);
		GroupDbo dbGroup = dao.read(groupId);
		GroupDbo dbChildGroup = dao.read(childGroupId);
		dbGroup.getChildren().remove(dbChildGroup);
	}
	
	public void addUser(Long groupId, Long userId) throws ValidationException {
		validator.validateUserAddition(groupId, userId);
		GroupDbo dbGroup = dao.read(groupId);
		UserDbo dbUser = userDao.read(userId);
		if(dbGroup.getUsers() == null) dbGroup.setUsers(new HashSet<UserDbo>());
		dbGroup.getUsers().add(dbUser);
	}
	
	public void removeUser(Long groupId, Long userId) throws ValidationException {
		validator.validateUserDeletion(groupId, userId);
		GroupDbo dbGroup = dao.read(groupId);
		UserDbo dbUser = userDao.read(userId);
		dbGroup.getUsers().remove(dbUser);
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
