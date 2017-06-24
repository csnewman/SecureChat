package com.securechat.plugins.defaultmanagers.client;

import java.util.ArrayList;
import java.util.List;

import com.securechat.api.client.IClientManager;
import com.securechat.api.client.chat.IClientChatManager;
import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.client.gui.IMainGui;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.DisconnectPacket;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.IPacketHandler;
import com.securechat.api.common.packets.UserListPacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.plugins.defaultmanagers.DefaultManagersPlugin;

/**
 * A reference implementation of the client manager.
 */
public class DefaultClientManager implements IClientManager {
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IGuiProvider guiProvider;
	@InjectInstance
	private IImplementationFactory factory;
	private IClientChatManager chatManager;
	private INetworkConnection connection;
	private IMainGui mainGui;
	private IConnectionProfile profile;
	private List<IPacketHandler> packetHandlers;

	@Override
	public void init() {
		packetHandlers = new ArrayList<IPacketHandler>();
	}

	@Override
	public void handleConnected(IConnectionProfile profile, INetworkConnection connection) {
		this.connection = connection;
		this.profile = profile;
		log.info("Connected");

		// Configures chat manager
		chatManager = factory.get(IClientChatManager.class, true);
		chatManager.init();

		// Takes over network control
		connection.setHandler(this::handlePacket);
		connection.setDisconnectHandler(this::handleError);

		// Opens main GUI
		mainGui = guiProvider.getMainGui();
		mainGui.init();
		mainGui.open();
	}

	@Override
	public void addPacketHandler(IPacketHandler handler) {
		packetHandlers.add(handler);
	}

	@Override
	public void removePacketHandler(IPacketHandler handler) {
		packetHandlers.remove(handler);
	}

	private void handlePacket(IPacket packet) {
		log.debug("Received Packet " + packet);
		if (packet instanceof DisconnectPacket) {
			log.info("Disconnected: " + ((DisconnectPacket) packet).getReason());
			mainGui.disconnected(((DisconnectPacket) packet).getReason());
		} else if (packet instanceof UserListPacket) {
			UserListPacket usp = (UserListPacket) packet;
			String[] names = usp.getUsernames();
			boolean[] online = usp.getOnline();

			mainGui.updateUserList(names, online);

			// Counts the number of online users
			int count = 0;
			for (boolean b : online) {
				if (b)
					count++;
			}
			mainGui.updateOnlineCount(count, names.length);
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

	@Override
	public void sendPacket(IPacket packet) {
		connection.sendPacket(packet);
	}

	private void handleError(String msg) {
		log.info("Internal Error: " + msg);
		mainGui.disconnected(msg);
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	@Override
	public IConnectionProfile getConnectionProfile() {
		return profile;
	}

	@Override
	public INetworkConnection getConnection() {
		return connection;
	}

	public IMainGui getMainGui() {
		return mainGui;
	}

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker(DefaultManagersPlugin.NAME, DefaultManagersPlugin.VERSION, "client_manager",
				"1.0.0");
	}

}
