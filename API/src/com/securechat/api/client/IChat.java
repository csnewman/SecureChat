package com.securechat.api.client;

import java.util.List;

public interface IChat {

	public String getId();

	public String getOtherUser();

	public boolean isProtected();

	public boolean unlock(String password);

	public boolean isUnlocked();

	public int getUnread();

	public void sendMessage(String text);

	public List<IMessage> getMessages();

}
