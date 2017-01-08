package com.securechat.client.connection;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.securechat.basicencryption.PasswordEncryption;
import com.securechat.basicencryption.RSAEncryption;
import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.security.ProtectedDataStore;
import com.securechat.common.security.SecurityUtils;

public class ConnectionInfo {
	private String serverName, serverIp, username;
	private int serverPort, code;
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public ConnectionInfo(ByteReader reader) throws IOException {
		serverName = reader.readString();
		serverIp = reader.readString();
		serverPort = reader.readInt();
		publicKey = RSAEncryption.loadPublicKey(reader.readArray());
		privateKey = RSAEncryption.loadPrivateKey(reader.readArray());
		code = reader.readInt();
		username = reader.readString();
	}

	public ConnectionInfo(File file, char[] password) throws IOException {
		ProtectedDataStore connectionStore = new ProtectedDataStore(file,
				new PasswordEncryption(SecurityUtils.secureHashChars(password)));
		connectionStore.load();
		ByteReader reader = connectionStore.getReader();
		serverName = reader.readString();
		serverIp = reader.readString();
		serverPort = reader.readInt();
		publicKey = RSAEncryption.loadPublicKey(reader.readArray());
	}

	public void complete(String username, PrivateKey privateKey, int code) {
		this.username = username;
		this.privateKey = privateKey;
		this.code = code;
	}

	public void write(ByteWriter writer) {
		writer.writeString(serverName);
		writer.writeString(serverIp);
		writer.writeInt(serverPort);
		writer.writeArray(RSAEncryption.savePublicKey(publicKey));
		writer.writeArray(RSAEncryption.savePrivateKey(privateKey));
		writer.writeInt(code);
		writer.writeString(username);
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public int getCode() {
		return code;
	}

	public String getUsername() {
		return username;
	}
}