package com.securechat.api.client.network;

/**
 * Represents the different states a new connection can be in when connecting to
 * a server for the first time.
 */
public enum EnumConnectionSetupStatus {
	/**
	 * The client is currently generating the key pair to be used.
	 */
	GeneratingKeyPair,
	/**
	 * The client is currently connecting to the server.
	 */
	Connecting,
	/**
	 * The client has been disconnected or has lost connection from the server.
	 */
	Disconnected,
	/**
	 * The client has requested the username from the server.
	 */
	RegisteringUsername,
	/**
	 * The server has responded with the fact that the username is invalid.
	 */
	UsernameInvalid,
	/**
	 * The server has responded with the fact that the username has been taken.
	 */
	UsernameTaken,
	/**
	 * The client is saving the registration information.
	 */
	Saving,
	/**
	 * The process has completed.
	 */
	Success

}
