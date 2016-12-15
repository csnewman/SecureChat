package com.securechat.server;

import java.io.File;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.security.ProtectedStore;
import com.securechat.common.security.RSAEncryption;

public class UserManager extends ProtectedStore{
	private static final File usersFile = new File("users.bin");
	private Map<String, User> users;
	
	public UserManager(KeyPair key) {
		super(usersFile, new RSAEncryption(key));
		users = new HashMap<String, User>();
	}

	public boolean doesUserExist(String name) {
		return users.containsKey(name);
	}
	
	public User getUser(String username) {
		return users.get(username);
	}

	public void registerUser(String username, PublicKey publicKey, int code) {
		if (doesUserExist(username))
			throw new RuntimeException("Username already in use");
		users.put(username, new User(username, publicKey, code));
		save();
	}

	@Override
	protected void loadContent(ByteReader bodyReader) {
		int size = bodyReader.readInt();
		users.clear();
		for(int i = 0; i < size; i++){
			User user = new User(bodyReader);
			users.put(user.getUsername(), user);
		}
	}

	@Override
	protected void writeContent(ByteWriter writer) {
		writer.writeInt(users.size());
		for(User user : users.values()){
			user.write(writer);
		}
	}

}
