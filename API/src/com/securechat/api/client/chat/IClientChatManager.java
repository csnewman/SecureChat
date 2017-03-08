package com.securechat.api.client.chat;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Stores and manages all open chats on the client.
 */
public interface IClientChatManager extends IImplementation {

	/**
	 * Configures the chat manager.
	 */
	void init();

	/**
	 * Attempts to start a chat with the other user.
	 * 
	 * @param username
	 *            the username of the other person
	 * @param protect
	 *            whether to encrypt the chat
	 * @param password
	 *            the password to use if encrypted
	 */
	void startChat(String username, boolean protect, String password);

	/**
	 * Checks whether a chat exists with a given user.
	 * 
	 * @param username
	 *            the username of the other person
	 * @return whether a chat exists
	 */
	boolean doesChatExist(String username);

	/**
	 * Gets the chat instance with the other user.
	 * 
	 * @param username
	 *            the username of the other person
	 * @return the chat instance
	 */
	IChat getChat(String username);

}
