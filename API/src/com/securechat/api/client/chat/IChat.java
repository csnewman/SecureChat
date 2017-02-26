package com.securechat.api.client.chat;

import java.util.List;

import com.securechat.api.common.implementation.IImplementation;

/**
 * An instance of a chat open with another user. Stores the local clients
 * information only.
 */
public interface IChat extends IImplementation {

	/**
	 * Gets the unique id of this chat.
	 * 
	 * @return chat id
	 */
	String getId();

	/**
	 * Gets the username of the other person the chat is between.
	 * 
	 * @return other persons username
	 */
	String getOtherUser();

	/**
	 * Checks whether this chat is protected by any form of encryption.
	 * 
	 * @return whether the chat is encrypted
	 */
	boolean isProtected();

	/**
	 * Attempts to decrypt the chat with the given password. Should not be
	 * called if the chat is unprotected.
	 * 
	 * @param password
	 *            the password to try and decrypt with
	 * @return whether the decrypt was successful
	 */
	boolean unlock(String password);

	/**
	 * Checks whether the chat is currently decrypted and the messages can be
	 * read. If the chat is unprotected it will always return true.
	 * 
	 * @return whether the messages are accessible
	 */
	boolean isUnlocked();

	/**
	 * Sets whether the user is currently reading this chat. Used to update the
	 * unread message count.
	 * 
	 * @param vis
	 *            whether the chat is open
	 */
	void markReading(boolean vis);

	/**
	 * Returns how many messages are yet to be read.
	 * 
	 * @return the unread messsage count
	 */
	int getUnread();

	/**
	 * The short id of the last message that was read
	 * 
	 * @return message short id
	 */
	int getLastReadId();

	/**
	 * Attempts to send a message.
	 * 
	 * @param text
	 *            the raw text
	 */
	void sendMessage(String text);

	/**
	 * Returns all messages that have been sent and received. Not in order.
	 * 
	 * @return all messages
	 */
	List<IMessage> getMessages();

	/**
	 * Checks whether the chat has fully loaded and is ready to be used.
	 * 
	 * @return whether the chat is loaded.
	 */
	boolean hasLoaded();

}
