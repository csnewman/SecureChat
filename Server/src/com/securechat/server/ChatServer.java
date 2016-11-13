package com.securechat.server;

import java.io.File;
import java.security.PublicKey;

import com.securechat.common.ByteWriter;
import com.securechat.common.security.ProtectedDataStore;
import com.securechat.common.security.ProtectedKeyStore;
import com.securechat.server.network.NetworkServer;

public class ChatServer {
	public static final String netBasePrivateKey = "netBasePrivate", netBasePublicKey = "netBasePublic";
	private NetworkServer networkServer;
	private ServerSettings settings;
	private ProtectedKeyStore store;
	private ProtectedDataStore connectionStore;

	public void start() {
		store = new ProtectedKeyStore(new File("data.pstore"), null);
		store.load();
		store.save();

		if (!store.keysExists(netBasePrivateKey, netBasePublicKey)) {
			store.generateKeyPair(netBasePrivateKey, netBasePublicKey);
			System.out.println("No network public and private key found! Generaring!");
		}

		connectionStore = new ProtectedDataStore(new File("clientConnectionInfo.blob"), null);
		ByteWriter connectionInfoWriter = new ByteWriter();
		connectionInfoWriter.writeString(settings.getServerName());
		connectionInfoWriter.writeString(settings.getPublicIp());
		connectionInfoWriter.writeInt(settings.getPort());
		connectionInfoWriter.writeArray(store.getKey(netBasePublicKey, PublicKey.class).getEncoded());
		connectionStore.save();

		settings = new ServerSettings();
		settings.load();

		networkServer = new NetworkServer(settings.getPort());
		networkServer.start();

	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.start();
	}

}
