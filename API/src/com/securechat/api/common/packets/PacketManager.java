package com.securechat.api.common.packets;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the management of packet ids.
 */
public class PacketManager {
	private static Map<String, Class<? extends IPacket>> idToPacketMap;
	private static Map<Class<? extends IPacket>, String> packetToIdMap;

	/**
	 * Registers the given packet to the given id.
	 * 
	 * @param id
	 *            the id to use over the network
	 * @param clazz
	 *            the packet class
	 */
	public static void registerPacket(String id, Class<? extends IPacket> clazz) {
		idToPacketMap.put(id, clazz);
		packetToIdMap.put(clazz, id);
	}

	/**
	 * Gets the network id of the given packet.
	 * 
	 * @param clazz
	 *            the packet to lookup
	 * @return the packets network id
	 */
	public static String getPacketId(Class<? extends IPacket> clazz) {
		return packetToIdMap.get(clazz);
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
			return idToPacketMap.get(id).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to create packet " + id);
		}
	}

	static {
		idToPacketMap = new HashMap<String, Class<? extends IPacket>>();
		packetToIdMap = new HashMap<Class<? extends IPacket>, String>();

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
