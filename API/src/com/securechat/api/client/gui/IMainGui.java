package com.securechat.api.client.gui;

import com.securechat.api.client.IChat;
import com.securechat.api.client.IClientManager;

public interface IMainGui extends IGui {

	public void init(IClientManager clientManager);

	public void updateUserList(String[] username, boolean[] online);

	public void updateChatList(IChat[] chats);

	public void updateOnlineCount(int count, int outOf);

	public void openChat(String username);
	
	public void updateMessages(String username);

}
