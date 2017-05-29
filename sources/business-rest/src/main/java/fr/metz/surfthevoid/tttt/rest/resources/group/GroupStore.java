package fr.metz.surfthevoid.tttt.rest.resources.group;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.GroupDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GroupDao;
import fr.metz.surfthevoid.tttt.rest.resources.ListStore;

@Named
public class GroupStore extends ListStore<Group, GroupDbo>{
	
	@Inject
	protected GroupDao dao;
	
	@Inject
	protected GroupValidator validator;
	
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
	protected Group extract(GroupDbo dbo) {
		Group Group = new Group();
		Group.setId(dbo.getId());
		Group.setName(dbo.getName());
		return Group;
	}

	@Override
	protected Group clean(Group res) {
		if(res != null){
			if(StringUtils.isNotEmpty(res.getName())){
				res.setName(res.getName().trim());
			}
		}
		return res;
	}
}
