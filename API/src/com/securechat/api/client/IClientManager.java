package com.securechat.api.client;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.IPacketHandler;

/**
 * Handles the basic client management once connected to a server.
 */
public interface IClientManager extends IImplementation {

	/**
	 * Called at client startup before the gui is loaded.
	 */
	void init();

	/**
	 * Injects a packet handler into the packet handling pipeline.
	 * 
	 * @param handler
	 *            the packet handler instance
	 */
	void addPacketHandler(IPacketHandler handler);

	/**
	 * Removes the previously added packet handler from the pipeline.
	 * 
	 * @param handler
	 *            the packet handler instance
	 */
	void removePacketHandler(IPacketHandler handler);

	/**
	 * Resets the client manager and transfers control of the network
	 * connection. Should only be called once fully connected to a server for
	 * general purpose use.
	 * 
	 * @param the
	 *            server profile used to connect
	 * @param connection
	 *            the active outgoing connection
	 */
	void handleConnected(IConnectionProfile profile, INetworkConnection connection);

	/**
	 * Sends a packet to the server.
	 * 
	 * @param packet
	 *            the packet instance
	 */
	void sendPacket(IPacket packet);

	/**
	 * Gets the current connection profile
	 * 
	 * @return the connection profile currently in use
	 */
	IConnectionProfile getConnectionProfile();

	/**
	 * Gets the current network connection
	 * 
	 * @return the current network connection
	 */
	INetworkConnection getConnection();

}
