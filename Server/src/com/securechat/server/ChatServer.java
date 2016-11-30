package com.securechat.server;

import java.io.File;
import java.security.PublicKey;

import com.securechat.common.ByteWriter;
import com.securechat.common.security.PasswordEncryption;
import com.securechat.common.security.ProtectedDataStore;
import com.securechat.common.security.ProtectedKeyStore;
import com.securechat.server.network.NetworkServer;

public class ChatServer {
	public static final String netBasePrivateKey = "netBasePrivate", netBasePublicKey = "netBasePublic";
	private static final File clientConnectionInfoFile = new File("clientConnectionInfo.scci");
	private NetworkServer networkServer;
	private ServerSettings settings;
	private ProtectedKeyStore store;
	private ProtectedDataStore connectionStore;

	public void start() {
		store = new ProtectedKeyStore(new File("data.pstore"), new PasswordEncryption(""));
		store.load();
		store.save();

		settings = new ServerSettings();
		settings.load();

		if (!store.keysExists(netBasePrivateKey, netBasePublicKey)) {
			store.generateKeyPair(netBasePrivateKey, netBasePublicKey);
			System.out.println("No network public and private key found! Generaring!");
		}

		connectionStore = new ProtectedDataStore(clientConnectionInfoFile, new PasswordEncryption(""));
		ByteWriter connectionInfoWriter = new ByteWriter();
		connectionInfoWriter.writeString(settings.getServerName());
		connectionInfoWriter.writeString(settings.getPublicIp());
		connectionInfoWriter.writeInt(settings.getPort());
		connectionInfoWriter.writeArray(store.getKey(netBasePublicKey, PublicKey.class).getEncoded());
		connectionStore.setContent(connectionInfoWriter);
		connectionStore.save();

		networkServer = new NetworkServer(settings.getPort());
		networkServer.start();

	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.start();
	}

}
