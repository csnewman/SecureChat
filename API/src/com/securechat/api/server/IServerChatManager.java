package com.securechat.api.server;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.server.users.IUser;

/**
 * Stores and manages all open chats on the server.
 */
public interface IServerChatManager extends IImplementation {

	/**
	 * Configures chat manager.
	 */
	void init();

	/**
	 * Handles the chat configuration for the connected user.
	 * 
	 * @param user
	 *            the connected user
	 */
	void onUserConnected(IUser user);

	/**
	 * Sends the list of all open chats to the user.
	 * 
	 * @param user
	 *            the user to send to
	 */
	void sendChatList(IUser user);
}
