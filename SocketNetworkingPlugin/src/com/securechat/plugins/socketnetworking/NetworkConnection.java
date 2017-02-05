package com.securechat.plugins.socketnetworking;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.INetworkConnection;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.PacketManager;
import com.securechat.api.common.plugins.Inject;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IHasher;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class NetworkConnection implements INetworkConnection {
	public static final ImplementationMarker MARKER = new ImplementationMarker(SocketNetworkingPlugin.NAME,
			SocketNetworkingPlugin.VERSION, "network_connection", "1.0.0");
	@Inject(associate = true)
	protected IHasher hasher;
	@InjectInstance
	protected IImplementationFactory factory;
	@InjectInstance
	protected ILogger log;
	protected Socket socket;
	protected IByteWriter writer;
	protected IByteReader reader;
	protected ReentrantLock sendLock;
	protected Thread readThread;
	protected boolean active;
	protected IAsymmetricKeyEncryption encryption;
	protected Consumer<String> disconnectHandler;
	protected Consumer<IPacket> handler;

	public NetworkConnection(Consumer<String> disconnectHandler, Consumer<IPacket> handler) {
		this.disconnectHandler = disconnectHandler;
		this.handler = handler;
	}

	public void init(Socket socket, IAsymmetricKeyEncryption encryption) {
		try {
			active = true;
			sendLock = new ReentrantLock();
			this.socket = socket;
			this.encryption = encryption;
			writer = IByteWriter.get(factory, "network_connection", socket.getOutputStream());
			reader = IByteReader.get(factory, "network_connection", socket.getInputStream());
			readThread = new Thread(this::readPackets, "ReadThread");
			readThread.start();
		} catch (IOException e) {
			throw new RuntimeException("Failed to connect", e);
		}
	}

	private void readPackets() {
		try {
			while (active) {
				byte[] ddata = encryption.decrypt(reader.readArray());
				IByteReader packetData = IByteReader.get(factory, "network_connection", ddata);
				IByteReader data = packetData.readReaderWithChecksum();

				String id = data.readString();
				IPacket packet = PacketManager.createPacket(id);
				packet.read(data);

				handler.accept(packet);
			}
		} catch (EOFException e) {
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
			disconnectHandler.accept("Internal Error (" + e.getMessage() + ":" + e.getClass() + ")");
		}
	}

	@Override
	public void sendPacket(IPacket packet) {
		try {
			IByteWriter packetData = IByteWriter.get(factory, "network_connection");
			packetData.writeString(PacketManager.getPacketId(packet.getClass()));
			packet.write(packetData);

			IByteWriter finalPacket = IByteWriter.get(factory, "network_connection");
			finalPacket.writeWriterWithChecksum(packetData);

			byte[] encryptedData = encryption.encrypt(finalPacket.toByteArray());

			sendLock.lock();
			writer.writeArray(encryptedData);
			sendLock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to send packet", e);
		}
	}

	@Override
	public void setEncryption(IAsymmetricKeyEncryption encryption) {
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
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
