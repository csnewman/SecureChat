package com.securechat.common.packets;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

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
	public void read(ByteReader reader) {
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
