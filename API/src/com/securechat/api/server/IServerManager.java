package com.securechat.api.server;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.server.users.IUser;

/**
 * Manages the connected users to the server
 */
public interface IServerManager extends IImplementation {

	/**
	 * Configures the server manager
	 */
	void init();

	/**
	 * Tracks the connected user and configures the user for general use.
	 * 
	 * @param user
	 *            the connected user
	 */
	void handleUserLogin(IUser user);

	/**
	 * Tracks when the user is disconnected and informs other users.
	 * 
	 * @param user
	 *            the disconnected user
	 */
	void handleUserLost(IUser user);

	/**
	 * Checks whether a user with the given username is currently online.
	 * 
	 * @param username
	 *            the user to check for
	 * @return whether the user is online
	 */
	boolean isUserOnline(String username);

	/**
	 * Gets the instance of the online user with the given username.
	 * 
	 * @param username
	 *            the username to lookup
	 * @return the instance of that user
	 */
	IUser getOnlineUser(String username);

}
