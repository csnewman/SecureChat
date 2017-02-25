package com.securechat.api.client.chat;

import com.securechat.api.common.implementation.IImplementation;

public interface IClientChatManager extends IImplementation{
	
	public void init();
	
	public boolean doesChatExist(String username);

	public IChat getChat(String username);

	public void startChat(String username, boolean protect, String password);

	
}
