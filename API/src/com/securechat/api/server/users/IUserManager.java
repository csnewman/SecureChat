package com.securechat.api.server.users;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Stores and handles the registration of users.
 */
public interface IUserManager extends IImplementation {

	/**
	 * Configures the user manager
	 */
	public void init();

	/**
	 * Checks whether a user exists with the given username.
	 * 
	 * @param username
	 *            the username to check for
	 * @return whether that users exists.
	 */
	public boolean doesUserExist(String username);

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
	public void registerUser(String username, byte[] publicKey, int clientCode);

	/**
	 * Returns the user associated with the given username.
	 * 
	 * @param username
	 *            the username to lookup
	 * @return the associated user
	 */
	public IUser getUser(String username);

	/**
	 * Returns all usernames known to the manager
	 * 
	 * @return all usernames
	 */
	public String[] getAllUsernames();

}
