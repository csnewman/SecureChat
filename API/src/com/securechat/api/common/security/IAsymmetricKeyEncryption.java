package com.securechat.api.common.security;

import java.io.IOException;

/**
 * Performs asymmetric encryption where the public key encrypts the data and the
 * private key decrypts the data.
 */
public interface IAsymmetricKeyEncryption extends IEncryption {

	/**
	 * Generates a new key
	 */
	void generate();

	/**
	 * Loads the key from the given data
	 * 
	 * @param publicKey
	 * @param privateKey
	 */
	void load(byte[] publicKey, byte[] privateKey) throws IOException;

	/**
	 * Returns the public key data
	 * 
	 * @return the public key
	 */
	byte[] getPublickey();

	/**
	 * Returns the private key data
	 * 
	 * @return the private key
	 */
	byte[] getPrivatekey();

}
