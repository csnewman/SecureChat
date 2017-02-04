package com.securechat.api.client.network;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;

public interface IClientNetworkManager {

	public IClientNetworkConnection openConnection(String host, int port, IAsymmetricKeyEncryption encryption,
			Consumer<String> disconnectHandler, Consumer<IPacket> packetHandler);

	public void setupConnection(IConnectionProfileProvider profileProvider, IConnectionProfile profile, String username,
			BiConsumer<EnumConnectionSetupStatus, String> statusConsumer);

}