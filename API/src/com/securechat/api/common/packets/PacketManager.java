package com.securechat.api.common.packets;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the management of packet ids.
 */
public class PacketManager {
	private static Map<String, Class<? extends IPacket>> ID_PACKET_MAP;
	private static Map<Class<? extends IPacket>, String> PACKET_ID_MAP;

	/**
	 * Registers the given packet to the given id.
	 * 
	 * @param id
	 *            the id to use over the network
	 * @param clazz
	 *            the packet class
	 */
	public static void registerPacket(String id, Class<? extends IPacket> clazz) {
		ID_PACKET_MAP.put(id, clazz);
		PACKET_ID_MAP.put(clazz, id);
	}

	/**
	 * Gets the network id of the given packet.
	 * 
	 * @param clazz
	 *            the packet to lookup
	 * @return the packets network id
	 */
	public static String getPacketId(Class<? extends IPacket> clazz) {
		return PACKET_ID_MAP.get(clazz);
	}

	/**
	 * Creates a new instance of the packet with the given network id.
	 * 
	 * @param id
	 *            the network id to lookup.
	 * @return the new packet instance
	 */
	public static IPacket createPacket(String id) {
		try {
			return ID_PACKET_MAP.get(id).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to create packet " + id);
		}
	}

	static {
		ID_PACKET_MAP = new HashMap<String, Class<? extends IPacket>>();
		PACKET_ID_MAP = new HashMap<Class<? extends IPacket>, String>();

		registerPacket("REGISTER", RegisterPacket.class);
		registerPacket("REGISTER_RESPONSE", RegisterResponsePacket.class);

		registerPacket("CONNECT", ConnectPacket.class);
		registerPacket("CHALLENGE", ChallengePacket.class);
		registerPacket("CHALLENGE_RESPONSE", ChallengeResponsePacket.class);
		registerPacket("CONNECTED", ConnectedPacket.class);

		registerPacket("DISCONNECT", DisconnectPacket.class);
		registerPacket("USER_LIST", UserListPacket.class);
		registerPacket("CHAT_LIST", ChatListPacket.class);
		registerPacket("CREATE_CHAT", CreateChatPacket.class);
		registerPacket("REQUEST_HISTORY", RequestMessageHistoryPacket.class);
		registerPacket("MESSAGE_HISTORY", MessageHistoryPacket.class);
		registerPacket("SEND_MESSAGE", SendMessagePacket.class);
		registerPacket("NEW_MESSAGE", NewMessagePacket.class);

	}
}
