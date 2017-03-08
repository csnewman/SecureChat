package com.securechat.api.common.network;

import java.util.function.Consumer;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.security.IEncryption;

/**
 * An active network connection.
 */
public interface INetworkConnection extends IImplementation {

	/**
	 * Sets the packet handler for incoming packets.
	 * 
	 * @param handler
	 *            the packet handler
	 */
	void setHandler(Consumer<IPacket> handler);

	/**
	 * Sets the disconnection handler for when the connection gets closed by the
	 * other end.
	 * 
	 * @param disconnectHandler
	 *            the disconnect handler
	 */
	void setDisconnectHandler(Consumer<String> disconnectHandler);

	/**
	 * Sets the packet handler to a specialised handler that only accepts one
	 * packet type or exits on an unexpected packet.
	 * 
	 * @param type
	 *            the accepted packet type
	 * @param handler
	 *            the packet handler
	 */
	<T extends IPacket> void setSingleHandler(Class<T> type, Consumer<T> handler);

	/**
	 * Sends a packet to the other end.
	 * 
	 * @param packet
	 *            the packet to send
	 */
	void sendPacket(IPacket packet);

	/**
	 * Sets the encryption to be used when sending and receiving packets.
	 * 
	 * @param key
	 *            the encryption to use
	 */
	void setEncryption(IEncryption encryption);

	/**
	 * Closes the network connection.
	 */
	void closeConnection();

}
