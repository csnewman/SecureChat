package com.securechat.common.packets;

import java.io.IOException;
import java.security.PublicKey;

import com.securechat.basicencryption.RSAEncryption;
import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public class RegisterPacket implements IPacket {
	private String username;
	private PublicKey publicKey;

	public RegisterPacket() {
	}

	public RegisterPacket(String username, PublicKey publicKey) {
		this.username = username;
		this.publicKey = publicKey;
	}

	@Override
	public void read(ByteReader reader) throws IOException {
		username = reader.readString();
		publicKey = RSAEncryption.loadPublicKey(reader.readArray());
	}

	@Override
	public void write(ByteWriter writer) {
		writer.writeString(username);
		writer.writeArray(RSAEncryption.savePublicKey(publicKey));
	}

	public String getUsername() {
		return username;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

}
