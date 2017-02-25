package com.securechat.plugins.defaultmanagers.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.database.FieldType;
import com.securechat.api.common.database.IDatabase;
import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataFormat;
import com.securechat.api.common.database.ObjectDataInstance;
import com.securechat.api.common.database.PrimitiveDataFormat;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.packets.ChatListPacket;
import com.securechat.api.common.packets.CreateChatPacket;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.MessageHistoryPacket;
import com.securechat.api.common.packets.NewMessagePacket;
import com.securechat.api.common.packets.RequestMessageHistoryPacket;
import com.securechat.api.common.packets.SendMessagePacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.server.IServerChatManager;
import com.securechat.api.server.IServerManager;
import com.securechat.api.server.users.IUser;
import com.securechat.plugins.defaultmanagers.DefaultManagersPlugin;

public class DefaultServerChatManager implements IServerChatManager {
	public static final ImplementationMarker MARKER = new ImplementationMarker(DefaultManagersPlugin.NAME,
			DefaultManagersPlugin.VERSION, "server_chat_manager", "1.0.0");
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IServerManager serverManager;
	@InjectInstance
	private IDatabase database;
	private ITable chatsTable, messagesTable;

	@Override
	public void init() {
		database.ensureTable("chats", CHATS_FORMAT);
		database.ensureTable("messages", MESSAGES_FORMAT);
		chatsTable = database.getTable("chats");
		messagesTable = database.getTable("messages");
	}

	@Override
	public void onUserConnected(IUser user) {
		user.addPacketHandler(p -> handlePacket(user, p));
	}

	public boolean handlePacket(IUser user, IPacket packet) {
		if (packet instanceof CreateChatPacket) {
			CreateChatPacket ccp = (CreateChatPacket) packet;
			String other = ccp.getUsername();
			if (other.equals(user.getUsername())) {
				user.disconnect("Can't create chat with self");
				return true;
			}

			ObjectDataInstance search = new ObjectDataInstance(CHATS_FORMAT);
			search.setField("user1", user.getUsername());
			search.setField("user2", other);
			ObjectDataInstance[] rows1 = chatsTable.getRows(search);

			search = new ObjectDataInstance(CHATS_FORMAT);
			search.setField("user1", other);
			search.setField("user2", user.getUsername());
			ObjectDataInstance[] rows2 = chatsTable.getRows(search);

			if (rows1.length > 0 || rows2.length > 0) {
				user.disconnect("Chat already exists");
				return true;
			}
			
			log.debug("Creating chat beween "+user.getUsername()+" and "+other);

			ObjectDataInstance row = new ObjectDataInstance(CHATS_FORMAT);
			row.setField("id", UUID.randomUUID().toString());
			row.setField("user1", user.getUsername());
			row.setField("user2", other);
			row.setField("protected", ccp.isProtected());
			row.setField("testdata", ccp.getTestData());
			row.setField("lastid", 0);
			chatsTable.insertRow(row);

			sendChatList(user);
			if (serverManager.isUserOnline(other)) {
				sendChatList(serverManager.getOnlineUser(other));
			}
		} else if (packet instanceof SendMessagePacket) {
			SendMessagePacket smp = (SendMessagePacket) packet;

			ObjectDataInstance row = chatsTable.getRow(smp.getId());

			if (row == null) {
				user.disconnect("Invalid chat id");
				return true;
			}

			if (!row.getField("user1", String.class).equals(user.getUsername())
					&& !row.getField("user2", String.class).equals(user.getUsername())) {
				user.disconnect("Not a member of chat");
				return true;
			}

			int mid = getNextMessageId(smp.getId());

			ObjectDataInstance nrow = new ObjectDataInstance(MESSAGES_FORMAT);
			nrow.setField("id", UUID.randomUUID().toString());
			nrow.setField("cid", smp.getId());
			nrow.setField("mid", mid);
			nrow.setField("sender", user.getUsername());
			nrow.setField("time", smp.getTime());
			nrow.setField("content", smp.getContent());
			messagesTable.insertRow(nrow);

			String other = row.getField("user1", String.class).equals(user.getUsername())
					? row.getField("user2", String.class) : row.getField("user1", String.class);

			NewMessagePacket newPacket = new NewMessagePacket(smp.getId(), user.getUsername(), mid, smp.getContent(),
					smp.getTime());

			user.sendPacket(newPacket);
			if (serverManager.isUserOnline(other)) {
				serverManager.getOnlineUser(other).sendPacket(newPacket);
			}
			return true;
		} else if (packet instanceof RequestMessageHistoryPacket) {
			RequestMessageHistoryPacket rmhp = (RequestMessageHistoryPacket) packet;

			ObjectDataInstance row = chatsTable.getRow(rmhp.getChatId());

			if (row == null) {
				user.disconnect("Invalid chat id");
				return true;
			}

			if (!row.getField("user1", String.class).equals(user.getUsername())
					&& !row.getField("user2", String.class).equals(user.getUsername())) {
				user.disconnect("Not a member of chat");
				return true;
			}

			ObjectDataInstance search = new ObjectDataInstance(MESSAGES_FORMAT);
			search.setField("cid", rmhp.getChatId());

			ObjectDataInstance[] rows = messagesTable.getRows(search);
			List<ObjectDataInstance> rowToSend = new ArrayList<ObjectDataInstance>();
			int lastId = 0;
			for (ObjectDataInstance frow : rows) {
				int id = frow.getField("mid", Integer.class);
				if (id > rmhp.getLastId()) {
					rowToSend.add(frow);
					if (id > lastId)
						lastId = id;
				}
			}

			String[] senders = new String[rowToSend.size()];
			long[] times = new long[rowToSend.size()];
			byte[][] contents = new byte[rowToSend.size()][];

			for (int i = 0; i < rowToSend.size(); i++) {
				ObjectDataInstance trow = rowToSend.get(i);
				senders[i] = trow.getField("sender", String.class);
				times[i] = trow.getField("time", Long.class);
				contents[i] = trow.getField("content", byte[].class);
			}

			user.sendPacket(new MessageHistoryPacket(rmhp.getChatId(), lastId, senders, times, contents));
			return true;
		}
		return false;
	}

	@Override
	public void sendChatList(IUser user) {
		ObjectDataInstance search = new ObjectDataInstance(CHATS_FORMAT);
		search.setField("user1", user.getUsername());
		ObjectDataInstance[] rows1 = chatsTable.getRows(search);

		search = new ObjectDataInstance(CHATS_FORMAT);
		search.setField("user2", user.getUsername());
		ObjectDataInstance[] rows2 = chatsTable.getRows(search);

		log.debug("Found " + rows1.length + "+" + rows2.length + " chats for " + user.getUsername());

		String[] chatIds = new String[rows1.length + rows2.length];
		String[] chatUsers = new String[rows1.length + rows2.length];
		boolean[] chatProtected = new boolean[rows1.length + rows2.length];
		int[] lastIds = new int[rows1.length + rows2.length];
		byte[][] testData = new byte[rows1.length + rows2.length][];

		for (int i = 0; i < rows1.length; i++) {
			ObjectDataInstance row = rows1[i];
			chatIds[i] = row.getField("id", String.class);
			chatUsers[i] = row.getField("user2", String.class);
			chatProtected[i] = row.getField("protected", Boolean.class);
			lastIds[i] = row.getField("lastid", Integer.class);
			testData[i] = row.getField("testdata", byte[].class);
		}

		for (int i = 0; i < rows2.length; i++) {
			ObjectDataInstance row = rows2[i];
			chatIds[rows1.length + i] = row.getField("id", String.class);
			chatUsers[rows1.length + i] = row.getField("user1", String.class);
			chatProtected[rows1.length + i] = row.getField("protected", Boolean.class);
			lastIds[rows1.length + i] = row.getField("lastid", Integer.class);
			testData[rows1.length + i] = row.getField("testdata", byte[].class);
		}

		user.sendPacket(new ChatListPacket(chatIds, chatUsers, chatProtected, lastIds, testData));
	}

	public int getNextMessageId(String id) {
		ObjectDataInstance row = chatsTable.getRow(id);
		int lid = row.getField("lastid", Integer.class) + 1;

		ObjectDataInstance data = new ObjectDataInstance(CHATS_FORMAT);
		data.setField("lastid", lid);
		chatsTable.updateRow(id, data);

		return lid;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ObjectDataFormat CHATS_FORMAT, MESSAGES_FORMAT;
	static {
		CHATS_FORMAT = new ObjectDataFormat();
		CHATS_FORMAT.addField("id", PrimitiveDataFormat.String, FieldType.Primary);
		CHATS_FORMAT.addField("user1", PrimitiveDataFormat.String, FieldType.Required);
		CHATS_FORMAT.addField("user2", PrimitiveDataFormat.String, FieldType.Required);
		CHATS_FORMAT.addField("lastid", PrimitiveDataFormat.Integer, FieldType.Required);
		CHATS_FORMAT.addField("protected", PrimitiveDataFormat.Boolean, FieldType.Required);
		CHATS_FORMAT.addField("testdata", PrimitiveDataFormat.ByteArray, FieldType.Required);

		MESSAGES_FORMAT = new ObjectDataFormat();
		MESSAGES_FORMAT.addField("id", PrimitiveDataFormat.String, FieldType.Primary);
		MESSAGES_FORMAT.addField("cid", PrimitiveDataFormat.String, FieldType.Required);
		MESSAGES_FORMAT.addField("mid", PrimitiveDataFormat.Integer, FieldType.Required);
		MESSAGES_FORMAT.addField("sender", PrimitiveDataFormat.String, FieldType.Required);
		MESSAGES_FORMAT.addField("time", PrimitiveDataFormat.Long, FieldType.Required);
		MESSAGES_FORMAT.addField("content", PrimitiveDataFormat.ByteArray, FieldType.Required);
	}
}
