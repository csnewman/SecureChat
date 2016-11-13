package com.securechat.client;

import java.io.File;
import java.security.PublicKey;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.security.ProtectedDataStore;
import com.securechat.common.security.SecurityUtils;

public class ConnectionInfo {
	private String serverName, serverIp;
	private int serverPort;
	private PublicKey publicKey;

	public void importFromFile(File file, String password) {
		ProtectedDataStore connectionStore = new ProtectedDataStore(file, password);
		connectionStore.load();
		ByteReader reader = connectionStore.getReader();
		serverName = reader.readString();
		serverIp = reader.readString();
		serverPort = reader.readInt();
		publicKey = SecurityUtils.loadPublicKey(reader.readArray());
	}

	public void load(ByteReader reader) {
		serverName = reader.readString();
		serverIp = reader.readString();
		serverPort = reader.readInt();
		publicKey = SecurityUtils.loadPublicKey(reader.readArray());
	}

	public void save(ByteWriter writer) {
		writer.writeString(serverName);
		writer.writeString(serverIp);
		writer.writeInt(serverPort);
		writer.writeArray(publicKey.getEncoded());
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

}