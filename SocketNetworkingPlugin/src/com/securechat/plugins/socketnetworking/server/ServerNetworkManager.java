package com.securechat.plugins.socketnetworking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PrimitiveProperty;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.server.network.IServerNetworkManager;
import com.securechat.plugins.socketnetworking.NetworkConnection;
import com.securechat.plugins.socketnetworking.SocketNetworkingPlugin;

/**
 * A reference implementation of the server network manager.
 */
public class ServerNetworkManager implements IServerNetworkManager {
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IContext context;
	private IAsymmetricKeyEncryption networkKey;
	private boolean active;
	private String serverName, publicIp;
	private int publicPort, bindPort;
	private ServerSocket serverSocket;
	private Thread listenThread;

	@Override
	public void init(IAsymmetricKeyEncryption networkKey) {
		this.networkKey = networkKey;

		PropertyCollection server = context.getSettings().getPermissive(SERVER_PROPERTY);
		serverName = server.getPermissive(NAME_PROPERY);

		PropertyCollection network = server.getPermissive(NETWORK_PROPERTY);
		publicIp = network.getPermissive(PIP_PROPERY);
		publicPort = network.getPermissive(PPORT_PROPERY);
		bindPort = network.getPermissive(BPORT_PROPERY);
	}

	@Override
	public IConnectionProfile generateProfile(IConnectionProfileProvider provider) {
		return provider.generateProfileTemplate(serverName, publicIp, publicPort, networkKey.getPublickey());
	}

	@Override
	public void start() {
		try {
			active = true;
			serverSocket = new ServerSocket(bindPort);
			listenThread = new Thread(this::handleConnections, "Listen Thread");
			listenThread.setDaemon(false);
			listenThread.start();
			log.info("Started network server!");
		} catch (IOException e) {
			throw new RuntimeException("Failed to start network server!", e);
		}
	}

	private void handleConnections() {
		while (active) {
			try {
				log.debug("Awaiting connection");
				Socket socket = serverSocket.accept();
				if (socket != null) {
					log.info("Client connected from " + socket.getRemoteSocketAddress() + "!");
					// Creates a connection handler
					EarlyConnectionHandler handler = new EarlyConnectionHandler(this);
					context.getImplementationFactory().inject(handler);
					handler.updateKey();

					// Creates a connection
					NetworkConnection connection = new NetworkConnection(handler::handleDisconnect,
							handler::handleFirstPacket);
					context.getImplementationFactory().inject(connection);

					// Configures network connection
					handler.setConnection(connection);
					connection.init(socket, handler.getKey());
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Connection failed! " + e.getMessage());
			}
		}
	}

	@Override
	public void stop() {
		try {
			active = false;
			serverSocket.close();
			listenThread.interrupt();
		} catch (IOException e) {
			throw new RuntimeException("Failed to stop network server!", e);
		}
	}

	public IAsymmetricKeyEncryption getNetworkKey() {
		return networkKey;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	private static final PrimitiveProperty<String> NAME_PROPERY, PIP_PROPERY;
	private static final PrimitiveProperty<Integer> PPORT_PROPERY;
	private static final PrimitiveProperty<Integer> BPORT_PROPERY;
	private static final CollectionProperty NETWORK_PROPERTY, SERVER_PROPERTY;
	static {
		MARKER = new ImplementationMarker(SocketNetworkingPlugin.NAME, SocketNetworkingPlugin.VERSION,
				"client_network_manager", "1.0.0");
		NAME_PROPERY = new PrimitiveProperty<String>("name", "Unnamed Server");
		PIP_PROPERY = new PrimitiveProperty<String>("public_ip", "localhost");
		PPORT_PROPERY = new PrimitiveProperty<Integer>("public_port", 1234);
		BPORT_PROPERY = new PrimitiveProperty<Integer>("bind_port", 1234);
		NETWORK_PROPERTY = new CollectionProperty("network", PIP_PROPERY, PPORT_PROPERY, BPORT_PROPERY);
		SERVER_PROPERTY = new CollectionProperty("server", NAME_PROPERY, NETWORK_PROPERTY);
	}

}
