package com.securechat.client.network;

import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.security.RSAEncryption;
import com.securechat.common.security.SecurityUtils;

public class NetworkClient {
	private Socket socket;
	private ByteWriter writer;
	private ByteReader reader;
	private ReentrantLock sendLock;
	private Thread readThread;
	private boolean active;
	private RSAEncryption encryption;
	private Consumer<ByteReader> handler;

	public void connect(String host, int port, PublicKey pub, PrivateKey pri) {
		try {
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
	
	public void disconnect(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setHandler(Consumer<ByteReader> handler) {
		this.handler = handler;
	}

	private void readPackets() {
		while (active) {
			byte[] ddata = encryption.decrypt(reader.readArray());
			ByteReader packet = new ByteReader(ddata);
			byte[] hash = packet.readArray();
			byte[] original = packet.readArray();
			byte[] checksum = SecurityUtils.hashData(original);

			System.out.println("[DEBUG] Read packet, rawLen='" + ddata.length + "', len='" + original.length
					+ "', hash='" + Arrays.equals(checksum, hash) + "'");

			if (!Arrays.equals(checksum, hash)) {
				throw new RuntimeException("Invalid checksum!");
			}

			ByteReader data = new ByteReader(original);
			handler.accept(data);
		}
	}

	public void sendPacket(ByteWriter data) {
		byte[] original = data.toByteArray();
		byte[] hash = SecurityUtils.hashData(original);

		ByteWriter packet = new ByteWriter();
		packet.writeArray(hash);
		packet.writeArray(original);

		byte[] encryptedData = encryption.encrypt(packet.toByteArray());

		sendLock.lock();
		writer.writeArray(encryptedData);
		sendLock.unlock();
	}

	public RSAEncryption getEncryption() {
		return encryption;
	}

}
