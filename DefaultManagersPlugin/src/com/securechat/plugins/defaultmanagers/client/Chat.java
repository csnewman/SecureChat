package com.securechat.plugins.defaultmanagers.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.securechat.api.client.IClientManager;
import com.securechat.api.client.chat.IChat;
import com.securechat.api.client.chat.IMessage;
import com.securechat.api.client.gui.IMainGui;
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

/**
 * A reference implementation of a chat.
 */
public class Chat implements IChat {
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
	private IMainGui mainGui;
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
		mainGui = chatManager.getMainGui();
		this.id = id;
		this.otherUser = otherUser;
		this.isProtected = isProtected;
		this.testData = testData;
		messages = new ArrayList<IMessage>();
	}

	public void load() throws IOException {
		IConnectionProfile profile = clientManager.getConnectionProfile();
		// Fetches a key from the key store
		key = factory.provide(IAsymmetricKeyEncryption.class, null, true);
		keystore.loadAsymmetricKeyOrGenerate("chat_" + id, key);

		// Checks if a chat cache exists
		path = "chatcache/" + profile.getName() + "/" + profile.getUsername() + "/" + id + ".bin";
		if (storage.doesFileExist(path)) {
			log.debug("Loading chat cache from " + path);
			IByteReader fileData = storage.readFile(path, key);
			try {
				IByteReader content = fileData.readReaderWithChecksum();
				// Reads last packet ids
				latestPacketId = content.readInt();
				lastReadId = content.readInt();

				// Reads each message
				int size = content.readInt();
				for (int i = 0; i < size; i++) {
					messages.add(
							new Message(content.readArray(), isProtected(), content.readString(), content.readLong()));
				}

				// Updates GUI with unread count
				mainGui.updateChatUnread(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void save() {
		log.debug("Writing chat cache to " + path);
		try {
			IByteWriter body = IByteWriter.get(factory);
			// Writes last packet ids
			body.writeInt(latestPacketId);
			body.writeInt(lastReadId);

			// Writes each message
			body.writeInt(messages.size());
			for (IMessage message : messages) {
				body.writeArray(message.getContent());
				body.writeString(message.getSender());
				body.writeLong(message.getTime());
			}

			IByteWriter finalData = IByteWriter.get(factory);
			finalData.writeWriterWithChecksum(body);
			storage.writeFile(path, key, finalData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean unlock(String password) {
		log.debug("Unlocking chat " + id);
		try {
			encryption = factory.provide(IPasswordEncryption.class);
			encryption.init(password.toCharArray());

			// Checks whether the encryption password is correct
			byte[] test = encryption.encrypt(DefaultClientChatManager.TEST);
			if (Arrays.equals(test, testData)) {
				unlocked = true;

				// Unlocks each message
				for (IMessage message : messages) {
					message.unlock(encryption);
				}

				if (messages.size() > 0)
					loaded = true;

				// Updates the GUI
				mainGui.updateMessages(otherUser);
				mainGui.updateChatUnread(this);

				log.debug("Unlocked chat");
				return true;
			}
		} catch (Exception e) {
			log.warning("Failed to unlock chat");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void sendMessage(String text) {
		// Ensures a message exists
		text = text.trim();
		if (text.length() == 0)
			return;

		// Ensures the chat is unlocked
		if (!isUnlocked())
			throw new RuntimeException("Chat is locked!");

		try {
			// Sends the message
			byte[] data = isProtected ? encryption.encrypt(text.getBytes()) : text.getBytes();
			clientManager.sendPacket(new SendMessagePacket(id, data, System.currentTimeMillis()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ensures that the last packet received is at least after the given id.
	 * 
	 * @param lastId
	 *            the id to ensure exists
	 */
	public void checkLast(int lastId) {
		if (latestPacketId >= lastId) {
			log.debug("Chat is up to date with the server");
			if (!loaded) {
				loaded = true;
				mainGui.updateMessages(otherUser);
			}
			return;
		}
		log.debug("Chat history is outdated, requesting");
		clientManager.sendPacket(new RequestMessageHistoryPacket(id, latestPacketId));
	}

	public void importMessages(IMessage[] newMessages, int lastId) {
		log.debug("Importing " + newMessages.length + " message/s (unlocked: " + isUnlocked() + ", protected: "
				+ isProtected + ")");

		// Updates last message id
		if (lastId > latestPacketId) {
			latestPacketId = lastId;
		}

		// Adds each message, decrypts if needed
		for (IMessage m : newMessages) {
			if (isUnlocked() && isProtected)
				m.unlock(encryption);
			messages.add(m);
		}

		if (isUnlocked())
			loaded = true;

		// Updates the last read message id
		if (reading) {
			lastReadId = latestPacketId;
		}

		// Flushes messages to cache
		save();

		// Updates GUI
		mainGui.updateChatUnread(this);
		mainGui.updateMessages(otherUser);
	}

	@Override
	public void markReading(boolean vis) {
		reading = vis;
		if (reading) {
			// Updates last read message id
			lastReadId = latestPacketId;
			// Flushes last read messasge id
			save();
		}
		// Updates GUI
		mainGui.updateChatUnread(this);
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

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker(DefaultManagersPlugin.NAME, DefaultManagersPlugin.VERSION, "chat", "1.0.0");
	}

}
