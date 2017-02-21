package com.securechat.plugins.socketnetworking.server;

import java.util.Random;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.RegisterPacket;
import com.securechat.api.common.packets.RegisterResponsePacket;
import com.securechat.api.common.packets.RegisterResponsePacket.RegisterStatus;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.server.users.IUserManager;
import com.securechat.plugins.socketnetworking.NetworkConnection;

public class EarlyConnectionHandler {
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IImplementationFactory factory;
	@InjectInstance
	private IUserManager userManager;
	private byte[] publicKey;
	private IAsymmetricKeyEncryption key;
	private ServerNetworkManager networkManager;
	private NetworkConnection connection;

	public EarlyConnectionHandler(ServerNetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	public void setConnection(NetworkConnection connection) {
		this.connection = connection;
	}

	public void updateKey() {
		key = factory.provide(IAsymmetricKeyEncryption.class, null, true, true, "network");
		key.load(publicKey, networkManager.getNetworkKey().getPrivatekey());

		if (connection != null) {
			connection.setEncryption(key);
		}
	}

	public void handleFirstPacket(IPacket rpacket) {
		if (rpacket instanceof RegisterPacket) {
			RegisterPacket packet = (RegisterPacket) rpacket;
			log.info("Handling register request for " + packet.getUsername());

			publicKey = packet.getPublicKey();
			updateKey();

			String username = packet.getUsername();

			if (userManager.doesUserExist(username)) {
				connection.sendPacket(new RegisterResponsePacket(RegisterStatus.UsernameTaken));
			} else {
				int code = new Random().nextInt();
				userManager.registerUser(username, publicKey, code);
				connection.sendPacket(new RegisterResponsePacket(code));
			}
			connection.setHandler(this::ignorePackets);
		} else {
			log.warning("Unknown First Packet: " + rpacket);
		}
	}

	private void ignorePackets(IPacket packet) {
	}

	public void handleDisconnect(String msg) {
		log.info("Disconnect: " + msg);
	}

	public IAsymmetricKeyEncryption getKey() {
		return key;
	}

}
