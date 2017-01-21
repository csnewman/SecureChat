package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class RegisterPacket implements IPacket {
	private String username;
	private byte[] publicKey;

	public RegisterPacket() {
	}

	public RegisterPacket(String username, byte[] publicKey) {
		this.username = username;
		this.publicKey = publicKey;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		username = reader.readString();
		publicKey = reader.readArray();
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeString(username);
		writer.writeArray(publicKey);
	}

	public String getUsername() {
		return username;
	}

	public byte[] getPublicKey() {
		return publicKey;
	}

}
