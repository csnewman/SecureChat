package com.securechat.plugins.defaultmanagers.server;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.securechat.api.common.IContext;
import com.securechat.api.common.database.FieldType;
import com.securechat.api.common.database.IDatabase;
import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataFormat;
import com.securechat.api.common.database.ObjectDataInstance;
import com.securechat.api.common.database.PrimitiveDataFormat;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PrimitiveProperty;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.server.users.IUser;
import com.securechat.api.server.users.IUserManager;
import com.securechat.plugins.defaultmanagers.DefaultManagersPlugin;

/**
 * A reference implementation of the user manager.
 */
public class DefaultUserManager implements IUserManager {
	@InjectInstance
	private IContext context;
	@InjectInstance
	private IDatabase database;
	@InjectInstance
	private IImplementationFactory factory;
	private ITable usersTable;
	private Map<String, User> users;
	private Pattern usernamePattern;

	@Override
	public void init() {
		users = new HashMap<String, User>();
		database.ensureTable("users", USERS_FORMAT);
		usersTable = database.getTable("users");

		// Loads the username format
		PropertyCollection collection = context.getSettings().getPermissive(USERS_PROPERTY);
		String nameFormat = collection.getPermissive(NAME_FORMAT_PROPERY);
		usernamePattern = Pattern.compile(nameFormat, Pattern.CASE_INSENSITIVE);
	}

	@Override
	public boolean doesUserExist(String username) {
		return getUser(username) != null;
	}

	@Override
	public boolean isUsernameValid(String username) {
		// Checks the username against the regex
		return usernamePattern.matcher(username).matches();
	}

	@Override
	public void registerUser(String username, byte[] publicKey, int clientCode) {
		if (doesUserExist(username))
			throw new RuntimeException("Username already in use");
		// Inserts the user
		ObjectDataInstance data = new ObjectDataInstance(USERS_FORMAT);
		data.setField("username", username);
		data.setField("pubkey", publicKey);
		data.setField("code", clientCode);
		usersTable.insertRow(data);

		User user = new User(data);
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
		User user = new User(row);
		factory.inject(user);
		users.put(username, user);
		return user;
	}

	@Override
	public String[] getAllUsernames() {
		// Goes through all rows and collects all usernames
		ObjectDataInstance[] rows = usersTable.getAllRows();
		String[] usernames = new String[rows.length];

		for (int i = 0; i < rows.length; i++) {
			usernames[i] = rows[i].getField("username", String.class);
		}

		return usernames;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	private static final ObjectDataFormat USERS_FORMAT;
	private static final PrimitiveProperty<String> NAME_FORMAT_PROPERY;
	private static final CollectionProperty USERS_PROPERTY;
	static {
		MARKER = new ImplementationMarker(DefaultManagersPlugin.NAME, DefaultManagersPlugin.VERSION, "user_manager",
				"1.0.0");

		USERS_FORMAT = new ObjectDataFormat();
		USERS_FORMAT.addField("username", PrimitiveDataFormat.String, FieldType.Primary);
		USERS_FORMAT.addField("pubkey", PrimitiveDataFormat.ByteArray, FieldType.Required);
		USERS_FORMAT.addField("code", PrimitiveDataFormat.Integer, FieldType.Required);

		// Default regex name format
		NAME_FORMAT_PROPERY = new PrimitiveProperty<String>("name_format", "^[a-zA-Z]\\w*$");
		USERS_PROPERTY = new CollectionProperty("users", NAME_FORMAT_PROPERY);
	}

}
