package com.securechat.plugins.socketnetworking.server;

import java.io.IOException;
import java.util.Random;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.packets.ChallengePacket;
import com.securechat.api.common.packets.ChallengeResponsePacket;
import com.securechat.api.common.packets.ConnectPacket;
import com.securechat.api.common.packets.DisconnectPacket;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.RegisterPacket;
import com.securechat.api.common.packets.RegisterResponsePacket;
import com.securechat.api.common.packets.RegisterResponsePacket.RegisterStatus;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.server.users.IUser;
import com.securechat.api.server.users.IUserManager;
import com.securechat.plugins.socketnetworking.NetworkConnection;

/**
 * Handles the initial configuration of a network connection.
 */
public class EarlyConnectionHandler {
	@InjectInstance
	private IContext context;
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
	private IUser user;
	private int tempCode;

	public EarlyConnectionHandler(ServerNetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	public void setConnection(NetworkConnection connection) {
		this.connection = connection;
	}

	public void updateKey() {
		key = factory.provide(IAsymmetricKeyEncryption.class);
		try {
			key.load(publicKey, networkManager.getNetworkKey().getPrivatekey());
		} catch (IOException e) {
			context.handleCrash(e);
		}

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

			// Ensures the username is valid
			if (!userManager.isUsernameValid(username)) {
				connection.sendPacket(new RegisterResponsePacket(RegisterStatus.UsernameInvalid));
			} else if (userManager.doesUserExist(username)) {
				connection.sendPacket(new RegisterResponsePacket(RegisterStatus.UsernameTaken));
			} else {
				// Creates a unique validation code
				int code = new Random().nextInt();
				userManager.registerUser(username, publicKey, code);
				connection.sendPacket(new RegisterResponsePacket(code));
			}
			connection.setHandler(this::ignorePackets);
		} else if (rpacket instanceof ConnectPacket) {
			ConnectPacket packet = (ConnectPacket) rpacket;
			String username = packet.getUsername();

			// Ensure user exists
			if (!userManager.doesUserExist(username)) {
				log.info("Client tried to login in as " + username + ", no account exists");
				disconnect("Unknown username");
			}

			// Ensure correct validation code was sent
			user = userManager.getUser(username);
			if (packet.getCode() != user.getClientCode()) {
				log.warning("[SECURITY] Client sent wrong code!");
				user = null;
				disconnect("Wrong code");
				return;
			}

			// Loads clients public key
			publicKey = user.getPublicKey();
			updateKey();

			// Sends a challenge for the client to complete
			tempCode = new Random().nextInt();
			connection.setSingleHandler(ChallengeResponsePacket.class, this::handleChallengeResponse);
			connection.sendPacket(new ChallengePacket(tempCode));
		} else {
			log.warning("Unknown First Packet: " + rpacket);
		}
	}

	private void handleChallengeResponse(ChallengeResponsePacket packet) {
		if (packet.getTempCode() != tempCode) {
			log.warning("[SECURITY] Client sent wrong temp code back!");
			disconnect("Wrong temp code");
			return;
		}

		log.info("Client logged in as " + user.getUsername());
		user.assignToConnection(connection);
	}

	private void disconnect(String msg) {
		connection.sendPacket(new DisconnectPacket(msg));
		connection.setHandler(this::ignorePackets);
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
