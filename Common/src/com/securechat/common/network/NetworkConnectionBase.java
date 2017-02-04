package com.securechat.common.network;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.PacketManager;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.common.security.SecurityUtils;

public abstract class NetworkConnectionBase {
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
				byte[] hash = packetData.readArray();
				byte[] original = packetData.readArray();
				byte[] checksum = SecurityUtils.hashData(original);

				log.debug("Read packet, rawLen='" + ddata.length + "', len='" + original.length + "', hash='"
						+ Arrays.equals(checksum, hash) + "'");

				if (!Arrays.equals(checksum, hash)) {
					throw new RuntimeException("Invalid checksum!");
				}

				IByteReader data = IByteReader.get(factory, "network_connection", original);
				String id = data.readString();
				IPacket packet = PacketManager.createPacket(id);
				packet.read(data);

				handlePacket(packet);
			}
		} catch (EOFException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
			}
			active = false;
			handleConnectionLost();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
			}
			active = false;
			handleInternalError(e.getMessage() + ":" + e.getClass());
		}
	}

	protected abstract void handlePacket(IPacket packet);

	protected abstract void handleConnectionLost();

	protected abstract void handleInternalError(String msg);

	public void sendPacket(IPacket packet) {
		try {
			IByteWriter packetData = IByteWriter.get(factory, "network_connection");
			packetData.writeString(PacketManager.getPacketId(packet.getClass()));
			packet.write(packetData);

			byte[] original = packetData.toByteArray();
			byte[] hash = SecurityUtils.hashData(original);

			IByteWriter finalPacket = IByteWriter.get(factory, "network_connection");
			finalPacket.writeArray(hash);
			finalPacket.writeArray(original);

			byte[] encryptedData = encryption.encrypt(finalPacket.toByteArray());

			sendLock.lock();
			writer.writeArray(encryptedData);
			sendLock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to send packet", e);
		}
	}
	
	public void setEncryption(IAsymmetricKeyEncryption encryption) {
		this.encryption = encryption;
	}

	protected void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		active = false;
	}

}
