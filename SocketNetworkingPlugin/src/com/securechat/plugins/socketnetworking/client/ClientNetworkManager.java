package com.securechat.plugins.socketnetworking.client;

import java.io.IOException;
import java.net.Socket;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.securechat.api.client.network.EnumConnectionSetupStatus;
import com.securechat.api.client.network.IClientNetworkConnection;
import com.securechat.api.client.network.IClientNetworkManager;
import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.RegisterPacket;
import com.securechat.api.common.packets.RegisterResponsePacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.plugins.socketnetworking.SocketNetworkingPlugin;

public class ClientNetworkManager implements IClientNetworkManager {
	public static final ImplementationMarker MARKER = new ImplementationMarker(SocketNetworkingPlugin.NAME,
			SocketNetworkingPlugin.VERSION, "client_network_manager", "1.0.0");
	@InjectInstance
	private IImplementationFactory factory;
	@InjectInstance
	private IConnectionStore connectionStore;

	@Override
	public IClientNetworkConnection openConnection(String host, int port, IAsymmetricKeyEncryption encryption,
			Consumer<String> disconnectHandler, Consumer<IPacket> packetHandler) {
		try {
			ClientNetworkConnection connection = new ClientNetworkConnection(disconnectHandler, packetHandler);
			factory.inject(connection);
			connection.init(new Socket(host, port), encryption);
			return connection;
		} catch (IOException e) {
			e.printStackTrace();
			disconnectHandler.accept("Connection failed");
		}

		return null;
	}

	@Override
	public void setupConnection(IConnectionProfileProvider profileProvider, IConnectionProfile profile, String username,
			BiConsumer<EnumConnectionSetupStatus, String> statusConsumer) {
		try {
			statusConsumer.accept(EnumConnectionSetupStatus.GeneratingKeyPair, null);
			IAsymmetricKeyEncryption pair = factory.provide(IAsymmetricKeyEncryption.class, null, true, true,
					"network");
			pair.generate();

			IAsymmetricKeyEncryption networkPair = factory.provide(IAsymmetricKeyEncryption.class, null, true, true,
					"network");
			networkPair.load(profile.getPublicKey(), pair.getPrivatekey());

			statusConsumer.accept(EnumConnectionSetupStatus.Connecting, null);
			Consumer<String> disconnectHandler = r -> {
				statusConsumer.accept(EnumConnectionSetupStatus.Disconnected, r);
			};
			IClientNetworkConnection connection = openConnection(profile.getIP(), profile.getPort(), networkPair,
					disconnectHandler, null);

			if (connection == null) {
				disconnectHandler.accept("Connection failed");
				return;
			}

			connection.setSingleHandler(RegisterResponsePacket.class, r -> {
				switch (r.getStatus()) {
				case Success:
					statusConsumer.accept(EnumConnectionSetupStatus.Saving, null);
					connection.disconnect();

					connectionStore.addProfile(
							profileProvider.createProfile(profile, username, r.getCode(), pair.getPrivatekey()));
					statusConsumer.accept(EnumConnectionSetupStatus.Success, null);
					break;
				case UsernameTaken:
					statusConsumer.accept(EnumConnectionSetupStatus.UsernameTaken, null);
					break;
				default:
					break;
				}
			});

			statusConsumer.accept(EnumConnectionSetupStatus.RegisteringUsername, null);
			connection.sendPacket(new RegisterPacket(username, pair.getPublickey()));
		} catch (Exception e) {
			e.printStackTrace();
			statusConsumer.accept(EnumConnectionSetupStatus.Disconnected, "Internal error");
		}

	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
