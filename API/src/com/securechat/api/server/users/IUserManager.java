package com.securechat.api.server.users;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Stores and handles the registration of users.
 */
public interface IUserManager extends IImplementation {

	/**
	 * Configures the user manager
	 */
	void init();

	/**
	 * Checks whether a user exists with the given username.
	 * 
	 * @param username
	 *            the username to check for
	 * @return whether that users exists.
	 */
	boolean doesUserExist(String username);

	/**
	 * Checks whether the given username is valid.
	 * 
	 * @param username
	 *            the username to check
	 * @return whether the username is valid.
	 */
	boolean isUsernameValid(String username);

	/**
	 * Registers a user.
	 * 
	 * @param username
	 *            the username to use
	 * @param publicKey
	 *            the clients public key
	 * @param clientCode
	 *            the client code for future validation
	 */
	void registerUser(String username, byte[] publicKey, int clientCode);

	/**
	 * Returns the user associated with the given username.
	 * 
	 * @param username
	 *            the username to lookup
	 * @return the associated user
	 */
	IUser getUser(String username);

	/**
	 * Returns all usernames known to the manager
	 * 
	 * @return all usernames
	 */
	String[] getAllUsernames();

}
