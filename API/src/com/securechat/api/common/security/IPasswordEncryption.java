package com.securechat.api.common.security;

/**
 * Encrypts data with a password.
 */
public interface IPasswordEncryption extends IEncryption {

	/**
	 * Initialises the encryption with the given password.
	 * 
	 * @param password
	 *            the password to use
	 */
	public void init(char[] password);

}
