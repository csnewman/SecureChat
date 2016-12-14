package com.securechat.server;

import java.security.PublicKey;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.security.RSAEncryption;

public class User {
	private String username;
	private PublicKey publicKey;
	private int code;

	public User(String username, PublicKey publicKey, int code) {
		this.username = username;
		this.publicKey = publicKey;
		this.code = code;
	}

	public User(ByteReader reader) {
		username = reader.readString();
		publicKey = RSAEncryption.loadPublicKey(reader.readArray());
		code = reader.readInt();
	}

	public void write(ByteWriter writer) {
		writer.writeString(username);
		writer.writeArray(RSAEncryption.savePublicKey(publicKey));
		writer.writeInt(code);
	}

	public String getUsername() {
		return username;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public int getCode() {
		return code;
	}

}
