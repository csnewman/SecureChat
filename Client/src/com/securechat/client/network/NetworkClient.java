package com.securechat.client.network;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import com.securechat.basicencryption.RSAEncryption;
import com.securechat.common.packets.IPacket;
import com.securechat.common.packets.PacketManager;
import com.securechat.common.security.SecurityUtils;
import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;

public class NetworkClient {
	private Socket socket;
	private ByteWriter writer;
	private ByteReader reader;
	private ReentrantLock sendLock;
	private Thread readThread;
	private boolean active;
	private RSAEncryption encryption;
	private Consumer<IPacket> handler;
	private Consumer<String> disconnectHandler;

	public void connect(String host, int port, PublicKey pub, PrivateKey pri, Consumer<String> disconnectHandler) {
		try {
			this.disconnectHandler = disconnectHandler;
			encryption = new RSAEncryption(pub, pri);
			active = true;
			sendLock = new ReentrantLock();
			socket = new Socket(host, port);
			writer = new ByteWriter(socket.getOutputStream());
			reader = new ByteReader(socket.getInputStream());
			readThread = new Thread(this::readPackets, "ReadThread");
			readThread.start();
			System.out.println("Connected to server!");
		} catch (IOException e) {
			throw new RuntimeException("Failed to connect", e);
		}
	}

	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setHandler(Consumer<IPacket> handler) {
		this.handler = handler;
	}

	public void setDisconnectHandler(Consumer<String> disconnectHandler) {
		this.disconnectHandler = disconnectHandler;
	}

	@SuppressWarnings("unchecked")
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
			}
		});
	}

	private void readPackets() {
		try {
			while (active) {
				byte[] ddata = encryption.decrypt(reader.readArray());
				ByteReader packetData = new ByteReader(ddata);
				byte[] hash = packetData.readArray();
				byte[] original = packetData.readArray();
				byte[] checksum = SecurityUtils.hashData(original);

				System.out.println("[DEBUG] Read packet, rawLen='" + ddata.length + "', len='" + original.length
						+ "', hash='" + Arrays.equals(checksum, hash) + "'");

				if (!Arrays.equals(checksum, hash)) {
					throw new RuntimeException("Invalid checksum!");
				}

				ByteReader data = new ByteReader(original);
				String id = data.readString();
				IPacket packet = PacketManager.createPacket(id);
				packet.read(data);

				handler.accept(packet);
			}
		} catch (EOFException e){
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
			}
			disconnectHandler.accept("Connection lost to server");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
			}
			disconnectHandler.accept("Internal Error (" + e.getMessage() + ":" + e.getClass() + ")");
		}

	}

	public void sendPacket(IPacket packet) {
		ByteWriter packetData = new ByteWriter();
		packetData.writeString(PacketManager.getPacketId(packet.getClass()));
		packet.write(packetData);

		byte[] original = packetData.toByteArray();
		byte[] hash = SecurityUtils.hashData(original);

		ByteWriter finalPacket = new ByteWriter();
		finalPacket.writeArray(hash);
		finalPacket.writeArray(original);

		byte[] encryptedData = encryption.encrypt(finalPacket.toByteArray());

		sendLock.lock();
		writer.writeArray(encryptedData);
		sendLock.unlock();
	}

	public RSAEncryption getEncryption() {
		return encryption;
	}

}
