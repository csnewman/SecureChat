package com.securechat.client.network;

import java.io.IOException;
import java.util.function.Consumer;

import com.securechat.api.client.network.IClientNetworkConnection;
import com.securechat.api.common.packets.IPacket;
import com.securechat.common.network.NetworkConnectionBase;

public class NetworkConnection extends NetworkConnectionBase implements IClientNetworkConnection {
	private Consumer<String> disconnectHandler;
	private Consumer<IPacket> handler;

	public NetworkConnection(Consumer<String> disconnectHandler, Consumer<IPacket> handler) {
		this.disconnectHandler = disconnectHandler;
		this.handler = handler;
	}

	@Override
	public void setHandler(Consumer<IPacket> handler) {
		this.handler = handler;
	}

	@Override
	public void setDisconnectHandler(Consumer<String> disconnectHandler) {
		this.disconnectHandler = disconnectHandler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void setSingleHandler(Class<T> type, Consumer<T> handler) {
		setHandler(p -> {
			if (type.isInstance(p)) {
				handler.accept((T) p);
			} else {
				disconnectHandler.accept("Unexpected packet " + p.getClass());
				try {
					socket.close();
				} catch (IOException e) {
				}
				active = false;
			}
		});
	}

	@Override
	protected void handlePacket(IPacket packet) {
		handler.accept(packet);
	}

	@Override
	protected void handleConnectionLost() {
		disconnectHandler.accept("Connection lost");
	}

	@Override
	protected void handleInternalError(String msg) {
		disconnectHandler.accept("Internal Error (" + msg + ")");
	}

	@Override
	public void disconnect() {
		closeConnection();
	}

}
