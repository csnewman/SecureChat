package com.securechat.api.common.security;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Converts raw data into a hash.
 */
public interface IHasher extends IImplementation {

	/**
	 * Hashes the data.
	 * 
	 * @param input
	 *            the data to hash
	 * @return the hash
	 */
	byte[] hashData(byte[] input);

	/**
	 * Hashes the char data
	 * 
	 * @param chars
	 *            the chars to hash
	 * @return the hash
	 */
	char[] hashChars(char[] chars);

}
