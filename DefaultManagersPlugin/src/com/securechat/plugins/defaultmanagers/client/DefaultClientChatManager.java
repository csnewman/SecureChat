package com.securechat.plugins.defaultmanagers.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.securechat.api.client.IClientManager;
import com.securechat.api.client.chat.IChat;
import com.securechat.api.client.chat.IClientChatManager;
import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.client.gui.IMainGui;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.packets.ChatListPacket;
import com.securechat.api.common.packets.CreateChatPacket;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.IPacketHandler;
import com.securechat.api.common.packets.MessageHistoryPacket;
import com.securechat.api.common.packets.NewMessagePacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IPasswordEncryption;
import com.securechat.plugins.defaultmanagers.DefaultManagersPlugin;

public class DefaultClientChatManager implements IClientChatManager, IPacketHandler {
	public static final ImplementationMarker MARKER = new ImplementationMarker(DefaultManagersPlugin.NAME,
			DefaultManagersPlugin.VERSION, "client_chat_manager", "1.0.0");
	public static final byte[] TEST = "TEST".getBytes();
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IGuiProvider guiProvider;
	@InjectInstance
	private IImplementationFactory factory;
	@InjectInstance
	private IClientManager clientManager;
	private IMainGui mainGui;
	private Map<String, Chat> chats;
	private Map<String, Chat> chatIdMap;
	private Map<String, String> tempChatCache;

	@Override
	public void init() {
		clientManager.addPacketHandler(this);
		mainGui = guiProvider.getMainGui();

		chats = new HashMap<String, Chat>();
		chatIdMap = new HashMap<String, Chat>();
		tempChatCache = new HashMap<String, String>();
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

			clientManager.sendPacket(new CreateChatPacket(username, test, protect));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean handlePacket(IPacket packet) {
		if (packet instanceof ChatListPacket) {
			ChatListPacket clp = (ChatListPacket) packet;
			String[] chatIds = clp.getChatIds();
			String[] chatUsers = clp.getChatUsers();
			boolean[] chatProtected = clp.getChatProtected();
			int[] lastIds = clp.getLastIds();
			byte[][] testData = clp.getTestData();

			for (int i = 0; i < chatIds.length; i++) {
				String user = chatUsers[i];
				if (!chats.containsKey(user)) {
					Chat chat = new Chat(this, chatIds[i], user, chatProtected[i], testData[i]);
					factory.inject(chat);
					chat.load();
					chats.put(user, chat);
					chatIdMap.put(chatIds[i], chat);
				}
				Chat chat = chats.get(user);
				if (tempChatCache.containsKey(user)) {
					if (!chat.unlock(tempChatCache.get(user))) {
						throw new RuntimeException("Unlock failed!");
					}
					tempChatCache.remove(user);
					mainGui.openChat(user);
				}
				chat.checkLast(lastIds[i]);
			}
			mainGui.updateChatList(chats.values().toArray(new Chat[0]));
			return true;
		} else if (packet instanceof MessageHistoryPacket) {
			MessageHistoryPacket mhp = (MessageHistoryPacket) packet;

			if (!chatIdMap.containsKey(mhp.getChatId())) {
				log.error("Chat id unknown");
				return true;
			}

			Chat chat = chatIdMap.get(mhp.getChatId());

			Message[] messages = new Message[mhp.getSenders().length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = new Message(mhp.getContents()[i], chat.isProtected(), mhp.getSenders()[i],
						mhp.getTimes()[i]);
			}

			chat.importMessages(messages, mhp.getLastId());
		} else if (packet instanceof NewMessagePacket) {
			NewMessagePacket nmp = (NewMessagePacket) packet;

			if (!chatIdMap.containsKey(nmp.getChatId())) {
				log.error("Chat id unknown");
				return true;
			}

			Chat chat = chatIdMap.get(nmp.getChatId());
			chat.importMessages(
					new Message[] { new Message(nmp.getContent(), chat.isProtected(), nmp.getSender(), nmp.getTime()) },
					nmp.getMessageId());
			return true;
		}
		return false;
	}

	public IMainGui getMainGui() {
		return mainGui;
	}

	public IClientManager getClientManager() {
		return clientManager;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}