package fr.metz.surfthevoid.tttt.rest.resources.group;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import fr.metz.surfthevoid.tttt.rest.db.entity.GroupDbo;
import fr.metz.surfthevoid.tttt.rest.resources.ListStore;
import fr.metz.surfthevoid.tttt.rest.resources.ResourceListBoundary;

/**
 * Rest access point to manipulate group resources
 */
@Named("groupBoundary")
public class GroupBoundary extends ResourceListBoundary<Group> implements IGroupBoundary{
	
	@Inject
	protected GroupStore groupStore;
	
	@Override
	protected ListStore<Group, GroupDbo> getStore() {
		return groupStore;
	}

	@Override
	public Response children(Integer groupId) {
		// TODO Auto-generated method stub
		return null;
	}
}
