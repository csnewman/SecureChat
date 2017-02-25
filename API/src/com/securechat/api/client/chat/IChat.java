package com.securechat.api.client.chat;

import java.util.List;

import com.securechat.api.common.implementation.IImplementation;

public interface IChat extends IImplementation{

	public String getId();

	public String getOtherUser();

	public boolean isProtected();

	public boolean unlock(String password);

	public boolean isUnlocked();

	public void markReading(boolean vis);
	
	public int getUnread();
	
	public int getLastReadId();

	public void sendMessage(String text);

	public List<IMessage> getMessages();
	
	public boolean hasLoaded();

}
