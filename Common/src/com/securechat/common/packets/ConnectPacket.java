package com.securechat.common.packets;

import java.io.IOException;

import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;

public class ConnectPacket implements IPacket {
	private String username;
	private int code;

	public ConnectPacket() {
	}

	public ConnectPacket(String username, int code) {
		this.username = username;
		this.code = code;
	}

	@Override
	public void read(ByteReader reader) throws IOException {
		username = reader.readString();
		code = reader.readInt();
	}

	@Override
	public void write(ByteWriter writer) {
		writer.writeString(username);
		writer.writeInt(code);
	}

	public String getUsername() {
		return username;
	}

	public int getCode() {
		return code;
	}

}
