package com.securechat.api.server;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.server.users.IUser;

public interface IServerChatManager extends IImplementation{
	
	public void init();
	
	public void onUserConnected(IUser user);
	
	public void sendChatList(IUser user);
}
