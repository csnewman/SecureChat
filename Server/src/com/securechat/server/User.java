package com.securechat.server;

import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataInstance;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.ConnectedPacket;
import com.securechat.api.common.packets.DisconnectPacket;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.server.users.IUser;

public class User implements IUser {
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "user", "1.0.0");
	private ITable usersTable;
	private String username;
	private byte[] publicKey;
	private int clientCode;
	private INetworkConnection connection;

	public User(ITable usersTable, ObjectDataInstance row) {
		this.usersTable = usersTable;
		username = row.getField("username", String.class);
		publicKey = row.getField("pubkey", byte[].class);
		clientCode = row.getField("code", Integer.class);
	}

	@Override
	public void assignToConnection(INetworkConnection newConnection) {
		if(connection != null){
			connection.sendPacket(new DisconnectPacket("Logged in from somewhere else"));
			connection.setHandler(r -> {});
			connection.setDisconnectHandler(r -> {});
		}
		connection = newConnection;
		connection.setHandler(this::handlePacket);
		connection.setDisconnectHandler(this::handleError);
		connection.sendPacket(new ConnectedPacket());
	}
	
	private void handlePacket(IPacket packet){
		
	}
	
	private void handleError(String msg){
		
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public byte[] getPublicKey() {
		return publicKey;
	}

	@Override
	public int getClientCode() {
		return clientCode;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
