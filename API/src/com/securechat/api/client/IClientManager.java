package com.securechat.api.client;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.IPacket;

public interface IClientManager extends IImplementation {

	public void init();

	public void handleConnected(IConnectionProfile profile, INetworkConnection connection);

	public boolean doesChatExist(String username);

	public IChat getChat(String username);

	public void startChat(String username, boolean protect, String password);

	public void sendPacket(IPacket packet);

	public IConnectionProfile getConnectionProfile();

	public INetworkConnection getConnection();

}
