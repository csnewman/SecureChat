package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class ChatListPacket implements IPacket {
	private String[] chatIds, chatUsers;
	private boolean[] chatProtected;
	private byte[][] testData;

	public ChatListPacket() {
	}

	public ChatListPacket(String[] chatIds, String[] chatUsers, boolean[] chatProtected, byte[][] testData) {
		this.chatIds = chatIds;
		this.chatUsers = chatUsers;
		this.chatProtected = chatProtected;
		this.testData = testData;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		int size = reader.readInt();
		chatIds = new String[size];
		chatUsers = new String[size];
		chatProtected = new boolean[size];
		testData = new byte[size][];
		for (int i = 0; i < size; i++) {
			chatIds[i] = reader.readString();
			chatUsers[i] = reader.readString();
			chatProtected[i] = reader.readBoolean();
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
	
	public byte[][] getTestData() {
		return testData;
	}

}