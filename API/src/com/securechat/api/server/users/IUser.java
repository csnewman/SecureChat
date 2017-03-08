package com.securechat.api.server.users;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.IPacketHandler;

/**
 * Stores information about a user on the server.
 */
public interface IUser extends IImplementation {

	/**
	 * Returns the username of the user.
	 * 
	 * @return the username of the user
	 */
	public String getUsername();

	/**
	 * Returns the public key given by the client on initial registration for
	 * use in all communications.
	 * 
	 * @return the users public key
	 */
	public byte[] getPublicKey();

	/**
	 * Returns the client code given during initial registration.
	 * 
	 * @return the client code
	 */
	public int getClientCode();

	/**
	 * Assigns the network connection to this user and completes the login
	 * process.
	 * 
	 * @param connection
	 *            the network connection
	 */
	public void assignToConnection(INetworkConnection connection);

	/**
	 * Adds a packet handler to the packet handling pipeline.
	 * 
	 * @param handler
	 *            the packet handler
	 */
	public void addPacketHandler(IPacketHandler handler);

	/**
	 * Removes a packet handler from the packet handling pipeline.
	 * 
	 * @param handler
	 *            the packet handler
	 */
	public void removePacketHandler(IPacketHandler handler);

	/**
	 * Sends a packet to user if they are online.
	 * 
	 * @param packet
	 *            the packet to send
	 */
	public void sendPacket(IPacket packet);

	/**
	 * Disconnects the client for the given reason.
	 * 
	 * @param reason
	 *            the reason
	 */
	public void disconnect(String reason);

}
