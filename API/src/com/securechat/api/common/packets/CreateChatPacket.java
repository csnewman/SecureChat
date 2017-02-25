package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class CreateChatPacket implements IPacket {
	private String username;
	private byte[] testData;
	private boolean protect;

	public CreateChatPacket() {
	}

	public CreateChatPacket(String username, byte[] testData, boolean protect) {
		this.username = username;
		this.testData = testData;
		this.protect = protect;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		username = reader.readString();
		protect = reader.readBoolean();
		testData = reader.readArray();
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeString(username);
		writer.writeBoolean(protect);
		writer.writeArray(testData);
	}

	public String getUsername() {
		return username;
	}

	public boolean isProtected() {
		return protect;
	}

	public byte[] getTestData() {
		return testData;
	}

}
