package com.securechat.api.common.packets;

/**
 * Handles a packet as part of the standard packet pipeline once a standard
 * connection is complete.
 */
public interface IPacketHandler {

	/**
	 * Handles the packet.
	 * 
	 * @param packet
	 *            the packet to handle
	 * @return whether this handler handled the packet
	 */
	boolean handlePacket(IPacket packet);

}
