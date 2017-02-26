package com.securechat.api.client.network;

import java.util.List;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.IConnectionProfile;

/**
 * Stores all connections that are known to this client.
 */
public interface IConnectionStore extends IImplementation {

	/**
	 * Loads connection store
	 */
	void init();

	/**
	 * Adds an update listener which is informed of any changes to the
	 * connection store.
	 * 
	 * @param listener
	 *            the listener instance
	 */
	void addUpdateListener(IConnectionStoreUpdateListener listener);

	/**
	 * Removes an update listener.
	 * 
	 * @param listener
	 *            the listener instance
	 */
	void removeUpdateListener(IConnectionStoreUpdateListener listener);

	/**
	 * Registers the given profile into the store.
	 * 
	 * @param profile
	 *            the completed connection profile
	 */
	void addProfile(IConnectionProfile profile);

	/**
	 * Returns all the connection profiles stored in this store.
	 * 
	 * @return all profiles stored
	 */
	List<IConnectionProfile> getProfiles();

}
