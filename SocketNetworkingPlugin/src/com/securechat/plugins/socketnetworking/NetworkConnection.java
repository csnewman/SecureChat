package com.securechat.plugins.socketnetworking;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.PacketManager;
import com.securechat.api.common.plugins.Inject;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IEncryption;
import com.securechat.api.common.security.IHasher;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * A reference implementation of a network connection.
 */
public class NetworkConnection implements INetworkConnection {
	@Inject
	private IHasher hasher;
	@InjectInstance
	private IContext context;
	@InjectInstance
	private IImplementationFactory factory;
	@InjectInstance
	private ILogger log;
	private Socket socket;
	private IByteWriter writer;
	private IByteReader reader;
	private ReentrantLock sendLock;
	private Thread readThread;
	private boolean active;
	private IEncryption encryption;
	private Consumer<String> disconnectHandler;
	private Consumer<IPacket> handler;

	public NetworkConnection(Consumer<String> disconnectHandler, Consumer<IPacket> handler) {
		this.disconnectHandler = disconnectHandler;
		this.handler = handler;
	}

	/**
	 * Initialises the connection ready for sending and receiving packets.
	 * 
	 * @param socket
	 * @param encryption
	 */
	public void init(Socket socket, IAsymmetricKeyEncryption encryption) {
		try {
			active = true;
			sendLock = new ReentrantLock();
			this.socket = socket;
			this.encryption = encryption;

			// Gets the input and output of the socket
			writer = IByteWriter.get(factory, socket.getOutputStream());
			reader = IByteReader.get(factory, socket.getInputStream());

			// Starts reading packets
			readThread = new Thread(this::readPackets, "ReadThread");
			readThread.start();
		} catch (IOException e) {
			throw new RuntimeException("Failed to connect", e);
		}
	}

	private void readPackets() {
		try {
			while (active) {
				// Reads the raw data and decrypts it
				byte[] ddata = encryption.decrypt(reader.readArray());

				// Checks the checksum
				IByteReader packetData = IByteReader.get(factory, ddata);
				IByteReader data = packetData.readReaderWithChecksum();

				// Reads the packet
				String id = data.readString();
				IPacket packet = PacketManager.createPacket(id);
				packet.read(data);

				// Handles the packet
				handler.accept(packet);
			}
		} catch (EOFException | SocketException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
			}
			active = false;
			disconnectHandler.accept("Connection lost");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
			}
			active = false;
			context.handleCrash(e);
		}
	}

	@Override
	public void sendPacket(IPacket packet) {
		try {
			// Writes the content of the packet
			IByteWriter packetData = IByteWriter.get(factory);
			packetData.writeString(PacketManager.getPacketId(packet.getClass()));
			packet.write(packetData);

			// Checksums the content
			IByteWriter finalPacket = IByteWriter.get(factory);
			finalPacket.writeWriterWithChecksum(packetData);

			// Encrypts the packet
			byte[] encryptedData = encryption.encrypt(finalPacket.toByteArray());

			// Sends the packet
			sendLock.lock();
			writer.writeArray(encryptedData);
			sendLock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to send packet", e);
		}
	}

	@Override
	public void setEncryption(IEncryption encryption) {
		this.encryption = encryption;
	}

	@Override
	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		active = false;
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
	public <T extends IPacket> void setSingleHandler(Class<T> type, Consumer<T> handler) {
		setHandler(p -> {
			// Checks packet is of correct type
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
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker(SocketNetworkingPlugin.NAME, SocketNetworkingPlugin.VERSION,
				"network_connection", "1.0.0");
	}

}
