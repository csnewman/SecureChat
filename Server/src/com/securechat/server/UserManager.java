package com.securechat.server;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.securechat.api.common.database.FieldType;
import com.securechat.api.common.database.IDatabase;
import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataFormat;
import com.securechat.api.common.database.ObjectDataInstance;
import com.securechat.api.common.database.PrimitiveDataFormat;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.server.users.IUser;
import com.securechat.api.server.users.IUserManager;

public class UserManager implements IUserManager {
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "user_manager",
			"1.0.0");
	@InjectInstance
	private IDatabase database;
	@InjectInstance
	private IImplementationFactory factory;
	private ITable usersTable, chatsTable, messagesTable;
	private Map<String, User> users;

	@Override
	public void init() {
		users = new HashMap<String, User>();

		database.ensureTable("users", USERS_FORMAT);
		database.ensureTable("chats", CHATS_FORMAT);
		database.ensureTable("messages", MESSAGES_FORMAT);

		usersTable = database.getTable("users");
		chatsTable = database.getTable("chats");
		messagesTable = database.getTable("messages");
	}

	@Override
	public boolean doesUserExist(String username) {
		return getUser(username) != null;
	}

	@Override
	public void registerUser(String username, byte[] publicKey, int clientCode) {
		if (doesUserExist(username))
			throw new RuntimeException("Username already in use");
		ObjectDataInstance data = new ObjectDataInstance(USERS_FORMAT);
		data.setField("username", username);
		data.setField("pubkey", publicKey);
		data.setField("code", clientCode);
		data.setField("plugindata", new JSONObject());
		usersTable.insertRow(data);
		User user = new User(this, data);
		factory.inject(user);
		users.put(username, user);
	}

	@Override
	public IUser getUser(String username) {
		if (users.containsKey(username))
			return users.get(username);
		ObjectDataInstance row = usersTable.getRow(username);
		if (row == null)
			return null;
		User user = new User(this, row);
		factory.inject(user);
		users.put(username, user);
		return user;
	}

	@Override
	public String[] getAllUsernames() {
		ObjectDataInstance[] rows = usersTable.getAllRows();
		String[] usernames = new String[rows.length];

		for (int i = 0; i < rows.length; i++) {
			usernames[i] = rows[i].getField("username", String.class);
		}

		return usernames;
	}

	public ITable getChatsTable() {
		return chatsTable;
	}

	public ITable getMessagesTable() {
		return messagesTable;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}
	
	public int getNextMessageId(String id){
		ObjectDataInstance row = chatsTable.getRow(id);
		int lid = row.getField("lastid", Integer.class) + 1;
		
		ObjectDataInstance data = new ObjectDataInstance(CHATS_FORMAT);
		data.setField("lastid", lid);
		chatsTable.updateRow(id, data);
		
		return lid;
	}

	public static final ObjectDataFormat USERS_FORMAT, CHATS_FORMAT, MESSAGES_FORMAT;
	static {
		USERS_FORMAT = new ObjectDataFormat();
		USERS_FORMAT.addField("username", PrimitiveDataFormat.String, FieldType.Primary);
		USERS_FORMAT.addField("pubkey", PrimitiveDataFormat.ByteArray, FieldType.Required);
		USERS_FORMAT.addField("code", PrimitiveDataFormat.Integer, FieldType.Required);
		USERS_FORMAT.addField("plugindata", PrimitiveDataFormat.JSON, FieldType.Required);

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
