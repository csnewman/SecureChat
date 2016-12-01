package com.securechat.server;

import java.io.Console;
import java.io.File;
import java.security.PublicKey;

import com.securechat.common.ByteWriter;
import com.securechat.common.security.PasswordEncryption;
import com.securechat.common.security.ProtectedDataStore;
import com.securechat.common.security.ProtectedKeyStore;
import com.securechat.common.security.RSAEncryption;
import com.securechat.common.security.SecurityUtils;
import com.securechat.server.network.NetworkServer;

public class ChatServer {
	public static final String netBasePrivateKey = "netBasePrivate", netBasePublicKey = "netBasePublic";
	private static final File clientConnectionInfoFile = new File("clientConnectionInfo.scci");
	private NetworkServer networkServer;
	private ServerSettings settings;
	private ProtectedKeyStore store;
	private ProtectedDataStore connectionStore;

	public void start() {
		System.out.println("SecureChat Server 1.0");
		Console console = System.console();
		char[] password;
		if (console == null) {
			System.out.println("No console found! Using default 'test' password for master keystore");
			password = new char[] { 't', 'e', 's', 't' };
			return;
		} else {
			password = console.readPassword("Master Keystore Password: ");
		}

		store = new ProtectedKeyStore(new File("data.pstore"),
				new PasswordEncryption(SecurityUtils.secureHashChars(password)));
		store.load();
		store.save();

		settings = new ServerSettings();
		settings.load();

		if (!store.keysExists(netBasePrivateKey, netBasePublicKey)) {
			System.out.println("No network public and private key found! Generaring!");
			store.generateKeyPair(netBasePrivateKey, netBasePublicKey);
		}

		connectionStore = new ProtectedDataStore(clientConnectionInfoFile, new RSAEncryption(store.loadKeyPair("")));
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
