package com.securechat.api.client.network;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;

/**
 * Manages the basic network communication.
 */
public interface IClientNetworkManager extends IImplementation {

	/**
	 * Opens a raw generic connection to a server for any purpose.
	 * 
	 * @param profile
	 *            the server information to connect to
	 * @param encryption
	 *            the encryption to use for packets
	 * @param disconnectHandler
	 *            the disconnect handler to use
	 * @param packetHandler
	 *            the packet handler to use
	 * @return the network connection
	 */
	INetworkConnection openConnection(IConnectionProfile profile, IAsymmetricKeyEncryption encryption,
			Consumer<String> disconnectHandler, Consumer<IPacket> packetHandler);

	/**
	 * Creates a new account on the given server with the given username.
	 * 
	 * @param profileProvider
	 *            the profile provider to use to generate the final completed
	 *            profile
	 * @param profile
	 *            the profile template to use to begin the initial connection
	 * @param username
	 *            the username to request from the server
	 * @param statusConsumer
	 *            the status handler
	 */
	void setupConnection(IConnectionProfileProvider profileProvider, IConnectionProfile profile, String username,
			BiConsumer<EnumConnectionSetupStatus, String> statusConsumer);

	/**
	 * Connects to the given server.
	 * 
	 * @param profile
	 *            the connection profile to connect to
	 * @param status
	 *            the status handler, boolean for whether connection succeeded
	 *            and the string for the fail reason.
	 */
	void connect(IConnectionProfile profile, BiConsumer<Boolean, String> status);

}
