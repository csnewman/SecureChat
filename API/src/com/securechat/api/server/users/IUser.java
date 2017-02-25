package com.securechat.api.server.users;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.IPacketHandler;

public interface IUser extends IImplementation {
	
	public String getUsername();

	public byte[] getPublicKey();
	
	public int getClientCode();

	public void assignToConnection(INetworkConnection connection);

	public void addPacketHandler(IPacketHandler handler);
	
	public void removePacketHandler(IPacketHandler handler);
	
	public void sendPacket(IPacket packet);

	public void disconnect(String string);

}
