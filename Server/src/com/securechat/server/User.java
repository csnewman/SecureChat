package com.securechat.server;

import java.util.List;
import java.util.UUID;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.database.FieldType;
import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataFormat;
import com.securechat.api.common.database.ObjectDataInstance;
import com.securechat.api.common.database.PrimitiveDataFormat;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.ChatListPacket;
import com.securechat.api.common.packets.ConnectedPacket;
import com.securechat.api.common.packets.CreateChatPacket;
import com.securechat.api.common.packets.DisconnectPacket;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.SendMessagePacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.server.IServerManager;
import com.securechat.api.server.users.IUser;

public class User implements IUser {
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "user", "1.0.0");
	@InjectInstance
	private ILogger log;
	private UserManager userManager;
	private String username;
	private byte[] publicKey;
	private int clientCode;
	private INetworkConnection connection;
	@InjectInstance
	private IServerManager serverManager;

	public User(UserManager userManager, ObjectDataInstance row) {
		this.userManager = userManager;
		username = row.getField("username", String.class);
		publicKey = row.getField("pubkey", byte[].class);
		clientCode = row.getField("code", Integer.class);
	}

	@Override
	public void assignToConnection(INetworkConnection newConnection) {
		if (connection != null) {
			disconnect("Logged in from somewhere else");
		}
		connection = newConnection;
		connection.setHandler(this::handlePacket);
		connection.setDisconnectHandler(this::handleError);
		connection.sendPacket(new ConnectedPacket());
		serverManager.handleUserLogin(this);
	}

	@Override
	public void sendChatList() {
		ITable ctable = userManager.getChatsTable();

		ObjectDataInstance search = new ObjectDataInstance(UserManager.CHATS_FORMAT);
		search.setField("user1", username);
		ObjectDataInstance[] rows1 = ctable.getRows(search);

		search = new ObjectDataInstance(UserManager.CHATS_FORMAT);
		search.setField("user2", username);
		ObjectDataInstance[] rows2 = ctable.getRows(search);

		String[] chatIds = new String[rows1.length + rows2.length];
		String[] chatUsers = new String[rows1.length + rows2.length];
		boolean[] chatProtected = new boolean[rows1.length + rows2.length];
		byte[][] testData = new byte[rows1.length + rows2.length][];

		for (int i = 0; i < rows1.length; i++) {
			ObjectDataInstance row = rows1[i];
			chatIds[i] = row.getField("id", String.class);
			chatUsers[i] = row.getField("user2", String.class);
			chatProtected[i] = row.getField("protected", Boolean.class);
			testData[i] = row.getField("testdata", byte[].class);
		}

		for (int i = 0; i < rows2.length; i++) {
			ObjectDataInstance row = rows2[i];
			chatIds[rows1.length + i] = row.getField("id", String.class);
			chatUsers[rows1.length + i] = row.getField("user1", String.class);
			chatProtected[rows1.length + i] = row.getField("protected", Boolean.class);
			testData[i] = row.getField("testdata", byte[].class);
		}

		sendPacket(new ChatListPacket(chatIds, chatUsers, chatProtected, testData));
	}

	private void handlePacket(IPacket packet) {
		log.debug("handle packet " + packet);
		if (packet instanceof CreateChatPacket) {
			CreateChatPacket ccp = (CreateChatPacket) packet;
			String other = ccp.getUsername();
			if (other.equals(username)) {
				disconnect("Can't create chat with self");
				return;
			}

			ITable ctable = userManager.getChatsTable();

			ObjectDataInstance search = new ObjectDataInstance(UserManager.CHATS_FORMAT);
			search.setField("user1", username);
			search.setField("user2", other);
			ObjectDataInstance[] rows1 = ctable.getRows(search);

			search = new ObjectDataInstance(UserManager.CHATS_FORMAT);
			search.setField("user1", other);
			search.setField("user2", username);
			ObjectDataInstance[] rows2 = ctable.getRows(search);

			if (rows1.length > 0 || rows2.length > 0) {
				disconnect("Chat already exists");
				return;
			}

			ObjectDataInstance row = new ObjectDataInstance(UserManager.CHATS_FORMAT);
			row.setField("id", UUID.randomUUID().toString());
			row.setField("user1", username);
			row.setField("user2", other);
			row.setField("protected", ccp.isProtected());
			row.setField("testdata", ccp.getTestData());
			row.setField("lastid", 0);
			ctable.insertRow(row);

			sendChatList();
			if (serverManager.isUserOnline(other)) {
				serverManager.getOnlineUser(other).sendChatList();
			}
		} else if (packet instanceof SendMessagePacket) {
			ITable ctable = userManager.getChatsTable();
			ITable mtable = userManager.getMessagesTable();
			SendMessagePacket smp = (SendMessagePacket) packet;

			ObjectDataInstance row = ctable.getRow(smp.getId());

			if (row == null) {
				disconnect("Invalid chat id");
				return;
			}

			if (row.getField("user1", String.class) != username && row.getField("user2", String.class) != username) {
				disconnect("Not a member of chat");
				return;
			}

			int mid = userManager.getNextMessageId(smp.getId());

			ObjectDataInstance nrow = new ObjectDataInstance(UserManager.MESSAGES_FORMAT);
			nrow.setField("id", UUID.randomUUID().toString());
			nrow.setField("cid", smp.getId());
			nrow.setField("mid", mid);
			nrow.setField("sender", username);
			nrow.setField("time", smp.getTime());
			nrow.setField("content", smp.getContent());
			mtable.insertRow(nrow);

			String other = row.getField("user1", String.class) != username ? row.getField("user1", String.class)
					: row.getField("user2", String.class);
			

//			if (serverManager.isUserOnline(other)) {
//				serverManager.getOnlineUser(other).sendChatList();
//			}
		}
	}

	
	
	private void handleError(String msg) {
		log.debug("Error occured: " + msg);
		connection.setHandler(r -> {
		});
		connection.setDisconnectHandler(r -> {
		});
		serverManager.handleUserLost(this);
	}

	private void disconnect(String msg) {
		try {
			connection.sendPacket(new DisconnectPacket(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
		connection.setHandler(r -> {
		});
		connection.setDisconnectHandler(r -> {
		});
		serverManager.handleUserLost(this);
	}

	@Override
	public void sendPacket(IPacket packet) {
		try {
			connection.sendPacket(packet);
		} catch (Exception e) {
			e.printStackTrace();
			handleError("Failed to send packet");
		}
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public byte[] getPublicKey() {
		return publicKey;
	}

	@Override
	public int getClientCode() {
		return clientCode;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
