package com.securechat.api.client.chat;

import com.securechat.api.common.security.IEncryption;

/**
 * An instance of a message in a chat.
 */
public interface IMessage {

	/**
	 * Attempts to decrypt the content of the message
	 * 
	 * @param encryption
	 *            the encryption used
	 */
	void unlock(IEncryption encryption);

	/**
	 * The raw content of the message. May or may not be encrypted.
	 * 
	 * @return the raw content
	 */
	byte[] getContent();

	/**
	 * The decrypted text of the message. Always returns a value even if the
	 * message is still encrypted.
	 * 
	 * @return the text of the message
	 */
	String getText();

	/**
	 * The username of the person who sent this message
	 * 
	 * @return the senders username
	 */
	String getSender();

	/**
	 * The system time in mills when the message was sent.
	 * 
	 * @return sent time
	 */
	long getTime();

}
