package com.securechat.api.client.gui;

import com.securechat.api.client.chat.IChat;

public interface IMainGui extends IGui {

	public void init();

	public void updateUserList(String[] username, boolean[] online);

	public void updateChatList(IChat[] chats);

	public void updateOnlineCount(int count, int outOf);

	public void openChat(String username);
	
	public void updateMessages(String username);

	public void disconnected(String msg);

	public void updateChatUnread(IChat chat);

}
