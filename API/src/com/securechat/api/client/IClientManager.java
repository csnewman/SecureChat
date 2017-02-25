package com.securechat.api.client;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.IPacketHandler;

public interface IClientManager extends IImplementation {

	public void init();
	
	public void addPacketHandler(IPacketHandler handler);
	
	public void removePacketHandler(IPacketHandler handler);

	public void handleConnected(IConnectionProfile profile, INetworkConnection connection);

	public void sendPacket(IPacket packet);

	public IConnectionProfile getConnectionProfile();

	public INetworkConnection getConnection();

}
