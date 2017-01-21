package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class NewChatPacket implements IPacket {
	private String username;

	public NewChatPacket() {
	}

	public NewChatPacket(String username) {
		this.username = username;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		username = reader.readString();
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeString(username);
	}

	public String getUsername() {
		return username;
	}

}
