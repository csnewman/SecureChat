package com.securechat.api.common.network;

import java.util.function.Consumer;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;

public interface INetworkConnection extends IImplementation {

	public void setHandler(Consumer<IPacket> handler);

	public void setDisconnectHandler(Consumer<String> disconnectHandler);

	public <T> void setSingleHandler(Class<T> type, Consumer<T> handler);

	public void sendPacket(IPacket packet);

	public void setEncryption(IAsymmetricKeyEncryption key);

	public void closeConnection();

}
