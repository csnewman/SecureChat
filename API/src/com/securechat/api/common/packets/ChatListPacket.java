package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by the server to inform the client of all open chats they have on the
 * server.
 */
public class ChatListPacket implements IPacket {
	private String[] chatIds, chatUsers;
	private boolean[] chatProtected;
	private int[] lastIds;
	private byte[][] testData;

	public ChatListPacket() {
	}

	public ChatListPacket(String[] chatIds, String[] chatUsers, boolean[] chatProtected, int[] lastIds,
			byte[][] testData) {
		this.chatIds = chatIds;
		this.chatUsers = chatUsers;
		this.chatProtected = chatProtected;
		this.lastIds = lastIds;
		this.testData = testData;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		int size = reader.readInt();
		chatIds = new String[size];
		chatUsers = new String[size];
		chatProtected = new boolean[size];
		lastIds = new int[size];
		testData = new byte[size][];
		for (int i = 0; i < size; i++) {
			chatIds[i] = reader.readString();
			chatUsers[i] = reader.readString();
			chatProtected[i] = reader.readBoolean();
			lastIds[i] = reader.readInt();
			testData[i] = reader.readArray();
		}
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeInt(chatIds.length);
		for (int i = 0; i < chatIds.length; i++) {
			writer.writeString(chatIds[i]);
			writer.writeString(chatUsers[i]);
			writer.writeBoolean(chatProtected[i]);
			writer.writeInt(lastIds[i]);
			writer.writeArray(testData[i]);
		}
	}

	public String[] getChatIds() {
		return chatIds;
	}

	public String[] getChatUsers() {
		return chatUsers;
	}

	public boolean[] getChatProtected() {
		return chatProtected;
	}

	public int[] getLastIds() {
		return lastIds;
	}

	public byte[][] getTestData() {
		return testData;
	}

}