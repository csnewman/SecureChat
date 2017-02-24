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
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.server.users.IUser;
import com.securechat.api.server.users.IUserManager;

public class UserManager implements IUserManager {
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "user_manager",
			"1.0.0");
	@InjectInstance
	private IDatabase database;
	private ITable usersTable;
	private Map<String, User> users;

	@Override
	public void init() {
		users = new HashMap<String, User>();

		database.ensureTable("users", USERS_FORMAT);
		usersTable = database.getTable("users");
	}

	@Override
	public boolean doesUserExist(String username) {
		if (users.containsKey(username))
			return true;
		ObjectDataInstance row = usersTable.getRow(username);
		if (row == null)
			return false;
		users.put(username, new User(usersTable, row));
		return true;
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
		users.put(username, new User(usersTable, data));
	}

	@Override
	public IUser getUser(String username) {
		if (users.containsKey(username))
			return users.get(username);
		ObjectDataInstance row = usersTable.getRow(username);
		if (row == null)
			return null;
		User user = new User(usersTable, row);
		users.put(username, user);
		return user;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	private static final ObjectDataFormat USERS_FORMAT;
	static {
		USERS_FORMAT = new ObjectDataFormat();
		USERS_FORMAT.addField("username", PrimitiveDataFormat.String, FieldType.Primary);
		USERS_FORMAT.addField("pubkey", PrimitiveDataFormat.ByteArray, FieldType.Required);
		USERS_FORMAT.addField("code", PrimitiveDataFormat.Integer, FieldType.Required);
		USERS_FORMAT.addField("plugindata", PrimitiveDataFormat.JSON, FieldType.Required);
	}

}
