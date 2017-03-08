package com.securechat.api.server.network;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;

/**
 * Manages incoming network connections.
 */
public interface IServerNetworkManager extends IImplementation {

	/**
	 * Configures the network manager.
	 * 
	 * @param networkKey
	 *            the key to encrypt communications with
	 */
	public void init(IAsymmetricKeyEncryption networkKey);

	/**
	 * Generates a new template profile with the connection information of the
	 * sever.
	 * 
	 * @param provider
	 *            the profile provider to use
	 * @return the new profile template
	 */
	public IConnectionProfile generateProfile(IConnectionProfileProvider provider);

	/**
	 * Starts the network manager and starts listening for clients.
	 */
	public void start();

	/**
	 * Stops the network manager and stops listening for clients.
	 */
	public void stop();

}
