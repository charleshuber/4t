package fr.metz.surfthevoid.tttt.rest.resources.user;

import javax.ws.rs.Path;

import fr.metz.surfthevoid.tttt.rest.resources.IResourceListBoundary;

@Path("/user")
public interface IUserBoundary extends IResourceListBoundary<User>{}
