package com.securechat.plugins.defaultmanagers.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.OperationsException;

import com.securechat.api.client.IClientManager;
import com.securechat.api.client.chat.IChat;
import com.securechat.api.client.chat.IMessage;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.packets.RequestMessageHistoryPacket;
import com.securechat.api.common.packets.SendMessagePacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.security.IPasswordEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;
import com.securechat.plugins.defaultmanagers.DefaultManagersPlugin;

public class Chat implements IChat {
	public static final ImplementationMarker MARKER = new ImplementationMarker(DefaultManagersPlugin.NAME,
			DefaultManagersPlugin.VERSION, "chat", "1.0.0");
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IImplementationFactory factory;
	@InjectInstance
	private IClientManager clientManager;
	@InjectInstance
	private IStorage storage;
	@InjectInstance
	private IKeystore keystore;
	private DefaultClientChatManager chatManager;
	private String id, otherUser, path;
	private int latestPacketId, lastReadId;
	private boolean isProtected, unlocked;
	private IPasswordEncryption encryption;
	private byte[] testData;
	private List<IMessage> messages;
	private boolean loaded, reading;
	private IAsymmetricKeyEncryption key;

	public Chat(DefaultClientChatManager chatManager, String id, String otherUser, boolean isProtected,
			byte[] testData) {
		this.chatManager = chatManager;
		this.id = id;
		this.otherUser = otherUser;
		this.isProtected = isProtected;
		this.testData = testData;
		messages = new ArrayList<IMessage>();
	}

	public void load() throws IOException {
		IConnectionProfile profile = clientManager.getConnectionProfile();
		path = "chatcache/" + profile.getName() + "/" + profile.getUsername() + "/" + id + ".bin";

		log.info("loading key");
		key = factory.provide(IAsymmetricKeyEncryption.class, null, true, true, MARKER.getId());
		keystore.loadAsymmetricKeyOrGenerate("chat_" + id, key);

		if (storage.doesFileExist(path)) {
			log.info("loading file");
			IByteReader fileData = storage.readFile(path, key);
			try {
				log.info("doing checksum");
				IByteReader content = fileData.readReaderWithChecksum();
				log.info("parsing");
				latestPacketId = content.readInt();
				lastReadId = content.readInt();

				int size = content.readInt();
				log.info("msg " + size);
				for (int i = 0; i < size; i++) {
					messages.add(
							new Message(content.readArray(), isProtected(), content.readString(), content.readLong()));
				}

				log.info("updating gui");
				chatManager.getMainGui().updateChatUnread(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.info("done");

	}

	private void save() {
		log.info("saving");
		try {
			IByteWriter body = IByteWriter.get(factory, MARKER.getId());
			body.writeInt(latestPacketId);
			body.writeInt(lastReadId);

			body.writeInt(messages.size());
			for (IMessage message : messages) {
				body.writeArray(message.getContent());
				body.writeString(message.getSender());
				body.writeLong(message.getTime());
			}

			IByteWriter finalData = IByteWriter.get(factory, MARKER.getId());
			log.info("doing checksum");
			finalData.writeWriterWithChecksum(body);
			log.info("writing");
			storage.writeFile(path, key, finalData);
			log.info("done");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				chatManager.getMainGui().updateChatUnread(this);

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
			if (!loaded) {
				loaded = true;
				chatManager.getMainGui().updateMessages(otherUser);
			}
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

		if (reading) {
			lastReadId = latestPacketId;
		}

		save();
		chatManager.getMainGui().updateChatUnread(this);
		chatManager.getMainGui().updateMessages(otherUser);
	}

	@Override
	public void markReading(boolean vis) {
		reading = vis;
		if (reading) {
			lastReadId = latestPacketId;
			save();
		}
		chatManager.getMainGui().updateChatUnread(this);
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
		return !isUnlocked() ? -1 : latestPacketId - lastReadId;
	}

	@Override
	public int getLastReadId() {
		return lastReadId;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
