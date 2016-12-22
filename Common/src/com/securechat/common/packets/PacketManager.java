package com.securechat.common.packets;

import java.util.HashMap;
import java.util.Map;

public class PacketManager {
	private static Map<String, Class<? extends IPacket>> idToPacketMap;
	private static Map<Class<? extends IPacket>, String> packetToIdMap;

	private static void registerPacket(String id, Class<? extends IPacket> clazz) {
		idToPacketMap.put(id, clazz);
		packetToIdMap.put(clazz, id);
	}

	public static String getPacketId(Class<? extends IPacket> clazz) {
		return packetToIdMap.get(clazz);
	}

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
//		registerPacket("CONNECTED", clazz);
		
		registerPacket("DISCONNECT", DisconnectPacket.class);
		registerPacket("NEW_CHAT", NewChatPacket.class);
	}
}
