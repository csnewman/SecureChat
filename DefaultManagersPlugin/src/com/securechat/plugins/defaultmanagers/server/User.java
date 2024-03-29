package com.securechat.plugins.defaultmanagers.server;

import java.util.ArrayList;
import java.util.List;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.database.ObjectDataInstance;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.ConnectedPacket;
import com.securechat.api.common.packets.DisconnectPacket;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.IPacketHandler;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.server.IServerManager;
import com.securechat.api.server.users.IUser;
import com.securechat.plugins.defaultmanagers.DefaultManagersPlugin;

/**
 * A reference implementation of the user.
 */
public class User implements IUser {
	@InjectInstance
	private ILogger log;
	private String username;
	private byte[] publicKey;
	private int clientCode;
	private INetworkConnection connection;
	@InjectInstance
	private IServerManager serverManager;
	private List<IPacketHandler> packetHandlers;

	public User(ObjectDataInstance row) {
		// Fetches the data from the row
		username = row.getField("username", String.class);
		publicKey = row.getField("pubkey", byte[].class);
		clientCode = row.getField("code", Integer.class);
		packetHandlers = new ArrayList<IPacketHandler>();
	}

	@Override
	public void assignToConnection(INetworkConnection newConnection) {
		if (connection != null) {
			disconnect("Logged in from somewhere else");
		}
		// Configures the connection
		connection = newConnection;
		connection.setHandler(this::handlePacket);
		connection.setDisconnectHandler(this::handleError);

		// Inform the client of being connected
		connection.sendPacket(new ConnectedPacket());

		// Informs the server manager of the user completing login
		serverManager.handleUserLogin(this);
	}

	private void handlePacket(IPacket packet) {
		log.debug("Received Packet " + packet);
		if (packet instanceof DisconnectPacket) {
			log.info("Disconnected: " + ((DisconnectPacket) packet).getReason());
		} else {
			// Try each packet handler in turn
			for (IPacketHandler handler : packetHandlers) {
				if (handler.handlePacket(packet)) {
					return;
				}
			}
			throw new RuntimeException("Unhandled packet! " + packet);
		}
	}

	private void handleError(String msg) {
		log.debug("Error occured: " + msg);
		connection.setHandler(this::ignorePackets);
		connection.setDisconnectHandler(this::ignoreDisconnect);
		serverManager.handleUserLost(this);
	}

	@Override
	public void disconnect(String msg) {
		try {
			connection.sendPacket(new DisconnectPacket(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
		connection.setHandler(this::ignorePackets);
		connection.setDisconnectHandler(this::ignoreDisconnect);
		serverManager.handleUserLost(this);
	}

	private void ignorePackets(IPacket packet) {
	}

	public void ignoreDisconnect(String msg) {
	}

	@Override
	public void addPacketHandler(IPacketHandler handler) {
		packetHandlers.add(handler);
	}

	@Override
	public void removePacketHandler(IPacketHandler handler) {
		packetHandlers.remove(handler);
	}

	@Override
	public void sendPacket(IPacket packet) {
		try {
			connection.sendPacket(packet);
		} catch (Exception e) {
			e.printStackTrace();
			handleError("Failed to send packet");
		}
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public byte[] getPublicKey() {
		return publicKey;
	}

	@Override
	public int getClientCode() {
		return clientCode;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker(DefaultManagersPlugin.NAME, DefaultManagersPlugin.VERSION, "user", "1.0.0");
	}

}
