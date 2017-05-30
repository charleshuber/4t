package fr.metz.surfthevoid.tttt.rest.resources.group;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;
import fr.metz.surfthevoid.tttt.rest.resources.user.User;

/**
 * Rest access point to manipulate group resources
 */
@Named("groupBoundary")
public class GroupBoundary extends ResourceBoundary<Group> implements IGroupBoundary{
	
	@Inject
	protected GroupStore groupStore;
	
	@Override
	protected GroupStore getStore() {
		return groupStore;
	}
	
	@Override
	public Response all() {
		ReadSetInterface<Group> operation = () -> groupStore.readAll();
		return readSet(operation);
	}

	@Override
	public Response children(Long groupId) {
		ReadSetInterface<Group> readSetAction = () -> groupStore.children(groupId);
		return readSet(readSetAction);
	}
	
	@Override
	public Response parents(Long groupId) {
		ReadSetInterface<Group> readSetAction = () -> groupStore.parents(groupId);
		return readSet(readSetAction);
	}
	
	@Override
	public Response users(Long groupId) {
		ReadSetInterface<User> readSetAction = () -> groupStore.users(groupId);
		return readSet(readSetAction);
	}
	
	@Override
	public Response addChild(Long groupId, Long childId) {
		return executePingAction(() -> groupStore.addChild(groupId, childId));
	}
	
	@Override
	public Response removeChild(Long groupId, Long childId) {
		return executePingAction(() -> groupStore.removeChild(groupId, childId));
	}
	
	@Override
	public Response addUser(Long groupId, Long userId) {
		return executePingAction(() -> groupStore.addUser(groupId, userId));
	}
	
	@Override
	public Response removeUser(Long groupId, Long userId) {
		return executePingAction(() -> groupStore.removeUser(groupId, userId));
	}
}
