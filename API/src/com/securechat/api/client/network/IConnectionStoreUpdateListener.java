package com.securechat.api.client.network;

/**
 * An update listener for a connection store. Informed of any changes made to a
 * connection store that it has been added to.
 */
public interface IConnectionStoreUpdateListener {

	/**
	 * Called when an update has occurred to a connection store.
	 */
	void onConnectionStoreUpdated();

}
