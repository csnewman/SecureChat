package com.securechat.common.packets;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public class NewChatPacket implements IPacket {
	private String username;

	public NewChatPacket() {
	}

	public NewChatPacket(String username) {
		this.username = username;
	}

	@Override
	public void read(ByteReader reader) {
		username = reader.readString();
	}

	@Override
	public void write(ByteWriter writer) {
		writer.writeString(username);
	}

	public String getUsername() {
		return username;
	}

}
