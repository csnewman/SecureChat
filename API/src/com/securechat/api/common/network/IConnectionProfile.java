package com.securechat.api.common.network;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Stores the connection information for a server.
 */
public interface IConnectionProfile extends IImplementation {

	/**
	 * Whether this profile is complete or is a template.
	 * 
	 * @return whether this is a template
	 */
	boolean isTemplate();

	/**
	 * Returns the name of the server.
	 * 
	 * @return the name of the server
	 */
	String getName();

	/**
	 * Returns the username associated with this account on the server if a
	 * completed profile.
	 * 
	 * @return the username
	 */
	String getUsername();

	/**
	 * Returns the IP of the server.
	 * 
	 * @return the server IP
	 */
	String getIP();

	/**
	 * Returns the port of the server.
	 * 
	 * @return the server port
	 */
	int getPort();

	/**
	 * Returns the authcode associated with this account on the server if a
	 * completed profile.
	 * 
	 * @return the authcode
	 */
	int getAuthCode();

	/**
	 * Returns the public network key of the server.
	 * 
	 * @return the public key
	 */
	byte[] getPublicKey();

	/**
	 * Returns the private key associated with this account on the server if a
	 * completed profile.
	 * 
	 * @return
	 */
	byte[] getPrivateKey();

}
