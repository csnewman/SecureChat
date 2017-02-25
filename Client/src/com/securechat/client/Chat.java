package com.securechat.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.securechat.api.client.IChat;
import com.securechat.api.client.IMessage;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.packets.SendMessagePacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IPasswordEncryption;

public class Chat implements IChat {
	@InjectInstance
	private IImplementationFactory factory;
	private ClientManager clientManager;
	private String id, otherUser;
	private boolean isProtected, unlocked;
	private IPasswordEncryption encryption;
	private byte[] testData;
	private List<IMessage> messages;

	public Chat(ClientManager clientManager, String id, String otherUser, boolean isProtected, byte[] testData) {
		this.clientManager = clientManager;
		this.id = id;
		this.otherUser = otherUser;
		this.isProtected = isProtected;
		this.testData = testData;
		messages = new ArrayList<IMessage>();

		messages.add(new Message("cool", "csnewman", System.currentTimeMillis()));
		messages.add(new Message("123", "csnewman", System.currentTimeMillis() + 10));
		messages.add(new Message("hi", otherUser, System.currentTimeMillis() + 20));
	}

	@Override
	public boolean unlock(String password) {
		try {
			encryption = factory.provide(IPasswordEncryption.class, null, true, true, "chat");
			encryption.init(password.toCharArray());
			byte[] test = encryption.encrypt(ClientManager.TEST);
			if(Arrays.equals(test, testData)){
				unlocked = true;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void sendMessage(String text) {
		text = text.trim();
		if (text.length() == 0)
			return;
		if(!isUnlocked())
			throw new RuntimeException("Chat is locked!");
		try {
			byte[]  data = encryption.encrypt(text.getBytes());
			clientManager.sendPacket(new SendMessagePacket(id, data, System.currentTimeMillis()));
		} catch (IOException e) {
			e.printStackTrace();
		}
//		messages.add(new Message(text, clientManager.getConnectionProfile().getUsername(), System.currentTimeMillis()));
//		clientManager.getMainGui().updateMessages(otherUser);
	}

	@Override
	public List<IMessage> getMessages() {
		return messages;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getOtherUser() {
		return otherUser;
	}

	@Override
	public boolean isProtected() {
		return isProtected;
	}

	@Override
	public boolean isUnlocked() {
		return !isProtected || unlocked;
	}

	@Override
	public int getUnread() {
		return !isUnlocked() ? -1 : 0;
	}

}
