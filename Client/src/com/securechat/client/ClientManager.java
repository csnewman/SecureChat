package com.securechat.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.securechat.api.client.IChat;
import com.securechat.api.client.IClientManager;
import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.client.gui.IMainGui;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.ChatListPacket;
import com.securechat.api.common.packets.CreateChatPacket;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.UserListPacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IPasswordEncryption;

public class ClientManager implements IClientManager {
	public static final byte[] TEST = "TEST".getBytes();
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "client_manager",
			"1.0.0");
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IGuiProvider guiProvider;
	@InjectInstance
	private IImplementationFactory factory;
	private INetworkConnection connection;
	private IMainGui mainGui;
	private IConnectionProfile profile;
	private Map<String, Chat> chats;
	private Map<String, String> tempChatCache;

	@Override
	public void init() {
		chats = new HashMap<String, Chat>();
		tempChatCache = new HashMap<String, String>();
	}

	@Override
	public void handleConnected(IConnectionProfile profile, INetworkConnection connection) {
		this.connection = connection;
		this.profile = profile;
		log.info("Connected");
		chats.clear();
		connection.setHandler(this::handlePacket);
		connection.setDisconnectHandler(this::handleError);

		mainGui = guiProvider.getMainGui();
		mainGui.init(this);
		mainGui.open();
	}

	@Override
	public boolean doesChatExist(String username) {
		return chats.containsKey(username) || tempChatCache.containsKey(username);
	}

	@Override
	public IChat getChat(String username) {
		return chats.get(username);
	}

	@Override
	public void startChat(String username, boolean protect, String password) {
		if (chats.containsKey(username)) {
			mainGui.openChat(username);
			return;
		}
		if (tempChatCache.containsKey(username)) {
			return;
		}
		try {
			IPasswordEncryption encryption = factory.provide(IPasswordEncryption.class, null, true, true, "chat");
			encryption.init(password.toCharArray());
			byte[] test = encryption.encrypt(TEST);

			tempChatCache.put(username, password);

			sendPacket(new CreateChatPacket(username, test, protect));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handlePacket(IPacket packet) {
		log.debug("HANDLE PACKET " + packet);
		if (packet instanceof UserListPacket) {
			UserListPacket usp = (UserListPacket) packet;
			String[] names = usp.getUsernames();
			boolean[] online = usp.getOnline();

			mainGui.updateUserList(names, online);

			int count = 0;
			for (boolean b : online) {
				if (b)
					count++;
			}
			mainGui.updateOnlineCount(count, names.length);
		} else if (packet instanceof ChatListPacket) {
			ChatListPacket clp = (ChatListPacket) packet;
			String[] chatIds = clp.getChatIds();
			String[] chatUsers = clp.getChatUsers();
			boolean[] chatProtected = clp.getChatProtected();
			byte[][] testData = clp.getTestData();

			for (int i = 0; i < chatIds.length; i++) {
				String user = chatUsers[i];
				if (!chats.containsKey(user)) {
					Chat chat = new Chat(this, chatIds[i], user, chatProtected[i], testData[i]);
					factory.inject(chat);
					chats.put(user, chat);
				}
				if (tempChatCache.containsKey(user)) {
					Chat chat = chats.get(user);
					if (!chat.unlock(tempChatCache.get(user))) {
						throw new RuntimeException("Unlock failed!");
					}
					tempChatCache.remove(user);
					mainGui.openChat(user);
				}
			}
			mainGui.updateChatList(chats.values().toArray(new Chat[0]));
		}
	}

	@Override
	public void sendPacket(IPacket packet) {
		connection.sendPacket(packet);
	}

	private void handleError(String msg) {
		log.debug("HANDLE ERROR " + msg);
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	@Override
	public IConnectionProfile getConnectionProfile() {
		return profile;
	}

	@Override
	public INetworkConnection getConnection() {
		return connection;
	}

}
