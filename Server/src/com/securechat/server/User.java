package com.securechat.server;

import java.io.IOException;
import java.security.PublicKey;

import com.securechat.basicencryption.RSAEncryption;
import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.packets.ConnectedPacket;
import com.securechat.common.packets.IPacket;
import com.securechat.server.network.NetworkClient;

public class User {
	private String username;
	private PublicKey publicKey;
	private int code;
	private NetworkClient network;

	public User(String username, PublicKey publicKey, int code) {
		this.username = username;
		this.publicKey = publicKey;
		this.code = code;
	}

	public User(ByteReader reader) throws IOException{
		username = reader.readString();
		publicKey = RSAEncryption.loadPublicKey(reader.readArray());
		code = reader.readInt();
	}

	public void write(ByteWriter writer) {
		writer.writeString(username);
		writer.writeArray(RSAEncryption.savePublicKey(publicKey));
		writer.writeInt(code);
	}

	public void handlePacket(IPacket packet) {
		System.out.println("HandlePacket "+packet);
	}

	public void assignToNetwork(NetworkClient client) {
		if (network != null) {
			network.disconnect("Logged in from somewhere else");
		}
		network = client;
		network.sendPacket(new ConnectedPacket());
	}

	public NetworkClient getNetwork() {
		return network;
	}

	public String getUsername() {
		return username;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public int getCode() {
		return code;
	}

}
