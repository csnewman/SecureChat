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
import com.securechat.plugins.socketnetworking.SocketNetworkingPlugin;

public class ServerNetworkManager implements IServerNetworkManager {
	public static final ImplementationMarker MARKER = new ImplementationMarker(SocketNetworkingPlugin.NAME,
			SocketNetworkingPlugin.VERSION, "client_network_manager", "1.0.0");
	private static final PrimitiveProperty<String> NAME_PROPERY = new PrimitiveProperty<String>("name",
			"Unnamed Server");
	private static final PrimitiveProperty<String> PIP_PROPERY = new PrimitiveProperty<String>("public_ip",
			"localhost");
	private static final PrimitiveProperty<Integer> PPORT_PROPERY = new PrimitiveProperty<Integer>("public_port", 1234);
	private static final PrimitiveProperty<Integer> BPORT_PROPERY = new PrimitiveProperty<Integer>("bind_port", 1234);
	private static final CollectionProperty NETWORK_PROPERTY = new CollectionProperty("network", PIP_PROPERY,
			PPORT_PROPERY, BPORT_PROPERY);
	private static final CollectionProperty SERVER_PROPERTY = new CollectionProperty("server", NAME_PROPERY,
			NETWORK_PROPERTY);
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
	// private ReentrantReadWriteLock clientLock;
	// private List<NetworkClient> clients;

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
			// clientLock = new ReentrantReadWriteLock();
			// clients = new ArrayList<NetworkClient>();
			serverSocket = new ServerSocket(bindPort);
			listenThread = new Thread(this::handleConnections, "Listen Thread");
			listenThread.setDaemon(false);
			listenThread.start();
			log.info("Started network server!");
		} catch (IOException e) {
			throw new RuntimeException("Failed to start network server!", e);
		}
		// TODO Auto-generated method stub
	}

	private void handleConnections() {
		while (active) {
			try {
				log.debug("Awaiting connection");
				Socket socket = serverSocket.accept();
				if (socket != null) {
					log.info("Client connected from " + socket.getRemoteSocketAddress() + "!");
					// NetworkClient client = new NetworkClient(server, socket);
					// clientLock.writeLock().lock();
					// clients.add(client);
					// clientLock.writeLock().unlock();
					// client.start();
				}
			} catch (Exception e) {
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

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
