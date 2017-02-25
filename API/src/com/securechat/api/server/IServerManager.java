package com.securechat.api.server;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.server.users.IUser;

public interface IServerManager extends IImplementation{
	
	public void init();
	
	public void handleUserLogin(IUser user);
	
	public void handleUserLost(IUser user);
	
	public boolean isUserOnline(String username);
	
	public IUser getOnlineUser(String username);
	
}
