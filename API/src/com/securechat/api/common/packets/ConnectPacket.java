package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by a client to the server to begin the login process as a given user.
 */
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
	public void read(IByteReader reader) throws IOException {
		username = reader.readString();
		code = reader.readInt();
	}

	@Override
	public void write(IByteWriter writer) {
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
