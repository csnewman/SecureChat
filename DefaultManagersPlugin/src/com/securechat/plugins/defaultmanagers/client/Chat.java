package com.securechat.plugins.defaultmanagers.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.securechat.api.client.IClientManager;
import com.securechat.api.client.chat.IChat;
import com.securechat.api.client.chat.IMessage;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.packets.RequestMessageHistoryPacket;
import com.securechat.api.common.packets.SendMessagePacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IPasswordEncryption;

public class Chat implements IChat {
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IImplementationFactory factory;
	@InjectInstance
	private IClientManager clientManager;
	private DefaultClientChatManager chatManager;
	private String id, otherUser;
	private int latestPacketId;
	private boolean isProtected, unlocked;
	private IPasswordEncryption encryption;
	private byte[] testData;
	private List<IMessage> messages;
	private boolean loaded;

	public Chat(DefaultClientChatManager clientManager, String id, String otherUser, boolean isProtected,
			byte[] testData) {
		this.chatManager = clientManager;
		this.id = id;
		this.otherUser = otherUser;
		this.isProtected = isProtected;
		this.testData = testData;
		messages = new ArrayList<IMessage>();
	}

	@Override
	public boolean unlock(String password) {
		try {
			encryption = factory.provide(IPasswordEncryption.class, null, true, true, "chat");
			encryption.init(password.toCharArray());
			byte[] test = encryption.encrypt(DefaultClientChatManager.TEST);
			if (Arrays.equals(test, testData)) {
				unlocked = true;

				for (IMessage message : messages) {
					message.unlock(encryption);
				}

				if (messages.size() > 0)
					loaded = true;

				chatManager.getMainGui().updateMessages(otherUser);
				
				log.info("Unlocked chat");
				return true;
			}
		} catch (Exception e) {
			log.error("Failed to unlock chat");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void sendMessage(String text) {
		text = text.trim();
		if (text.length() == 0)
			return;
		if (!isUnlocked())
			throw new RuntimeException("Chat is locked!");
		try {
			byte[] data = isProtected ? encryption.encrypt(text.getBytes()) : text.getBytes();
			clientManager.sendPacket(new SendMessagePacket(id, data, System.currentTimeMillis()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkLast(int lastId) {
		if (latestPacketId >= lastId) {
			log.debug("Chat up to date");
			loaded = true;
			return;
		}
		log.info("Missing chat messages, requesting");
		clientManager.sendPacket(new RequestMessageHistoryPacket(id, latestPacketId));
	}

	public void importMessages(IMessage[] newMessages, int lastId) {
		if (lastId > latestPacketId) {
			latestPacketId = lastId;
		}
		log.debug("Importing " + newMessages.length + " message/s (unlocked: " + isUnlocked() + ", protected: "
				+ isProtected + ")");
		for (IMessage m : newMessages) {
			if (isUnlocked() && isProtected)
				m.unlock(encryption);
			messages.add(m);
		}
		if (isUnlocked())
			loaded = true;
		chatManager.getMainGui().updateMessages(otherUser);
	}

	@Override
	public boolean hasLoaded() {
		return loaded;
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
