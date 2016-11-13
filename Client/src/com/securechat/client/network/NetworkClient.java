package com.securechat.client.network;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.security.SecurityUtils;

public class NetworkClient {
	private Socket socket;
	private ByteWriter writer;
	private ByteReader reader;
	private ReentrantLock sendLock;
	private Thread readThread;
	private boolean active;

	public void connect(String host, int port) {
		try {
			active = true;
			socket = new Socket(host, port);
			writer = new ByteWriter(socket.getOutputStream());
			reader = new ByteReader(socket.getInputStream());
			readThread = new Thread(this::readPackets, "ReadThread");
			readThread.start();
			System.out.println("Connected!");
		} catch (IOException e) {
			throw new RuntimeException("Failed to connect", e);
		}
	}

	private void readPackets() {
		while (active) {
			ByteReader packet = reader.readReaderContent();
			byte[] rawData = packet.getRawData();
			System.out.println("[DEBUG] Read packet, len="+rawData.length+", content='"+new String(rawData)+"', hash='"+new String(SecurityUtils.hashData(rawData))+"'");
			System.out.println("Handshake: "+packet.readString());
		}
	}

	public void sendPacket(ByteWriter data) {
		sendLock.lock();
		writer.writeWriterContent(data);
		sendLock.unlock();
	}

}
