package com.securechat.api.common.security;

import java.io.IOException;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Stores encryption keys in a password protected store.
 */
public interface IKeystore extends IImplementation {

	/**
	 * Generates a new keystore.
	 * 
	 * @param password
	 *            the password to use
	 * @return whether the keystore was created
	 */
	public boolean generate(char[] password);

	/**
	 * Loads the keystore
	 * 
	 * @param password
	 *            the password to use to unlock
	 * @return whether the keystore was loaded
	 */
	public boolean load(char[] password);

	/**
	 * Stores a new asymmetric key into the keystore.
	 * 
	 * @param name
	 *            the name to store it under
	 * @param publicKey
	 *            the public key
	 * @param privateKey
	 *            the private key
	 */
	public void addAsymmetricKey(String name, byte[] publicKey, byte[] privateKey);

	/**
	 * Stores a new asymmetric key into the keystore.
	 * 
	 * @param name
	 *            the name to store it under
	 * @param encryption
	 *            the key to store
	 */
	public void addAsymmetricKey(String name, IAsymmetricKeyEncryption encryption);

	/**
	 * Fetches the public key with the given name
	 * 
	 * @param name
	 *            the name to fetch
	 * @return the public key
	 */
	public byte[] getAsymmetricPublicKey(String name);

	/**
	 * Fetches the private key with the given name
	 * 
	 * @param name
	 *            the name to fetch
	 * @return the private key
	 */
	public byte[] getAsymmetricPrivateKey(String name);

	/**
	 * Loads the public and private keys into the given key.
	 * 
	 * @param name
	 *            the name to fetch
	 * @param encryption
	 *            the key to load into
	 */
	public void loadAsymmetricKey(String name, IAsymmetricKeyEncryption encryption) throws IOException;

	/**
	 * Loads the public and private keys into the given key if it exists
	 * otherwise generates a new key.
	 * 
	 * @param name
	 *            the name to fetch
	 * @param encryption
	 *            the key to load into
	 */
	public void loadAsymmetricKeyOrGenerate(String name, IAsymmetricKeyEncryption encryption) throws IOException;

	/**
	 * Checks whether a public key with the given name exists.
	 * 
	 * @param name
	 *            the name to check
	 * @return whether the key exists
	 */
	public boolean hasAsymmetricPublicKey(String name);

	/**
	 * Checks whether a private key with the given name exists.
	 * 
	 * @param name
	 *            the name to check
	 * @return whether the key exists
	 */
	public boolean hasAsymmetricPrivateKey(String name);

	/**
	 * Checks whether a public and private key pair with the given name exists.
	 * 
	 * @param name
	 *            the name to check
	 * @return whether the key pair exists
	 */
	public boolean hasAsymmetricKey(String name);

	/**
	 * Checks whether a keystore has been generated.
	 * 
	 * @return whether the keystore exists
	 */
	public boolean exists();

	/**
	 * Returns whether the keystore has been loaded.
	 * 
	 * @return whether the keystore is loaded
	 */
	public boolean isLoaded();

}
