package com.securechat.api.common.security;

import java.io.IOException;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Encrypts data to a secure format.
 */
public interface IEncryption extends IImplementation {

	/**
	 * Encrypts the content of the array.
	 * 
	 * @param data
	 *            the data to encrypt
	 * @return the encrypted data
	 * @throws IOException
	 *             if an error occurred
	 */
	byte[] encrypt(byte[] data) throws IOException;

	/**
	 * Decrypts the content of the array
	 * 
	 * @param data
	 *            the data to decrypt
	 * @return the decrypted data
	 * @throws IOException
	 *             if an error occured
	 */
	byte[] decrypt(byte[] data) throws IOException;

}
