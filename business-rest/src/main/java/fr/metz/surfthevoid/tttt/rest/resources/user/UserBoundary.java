package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import fr.metz.surfthevoid.tttt.rest.resources.ResourceBoundary;

@Service("userBoundary")
public class UserBoundary extends ResourceBoundary<User> implements IUserBoundary{

	@Override
	public Response read(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
