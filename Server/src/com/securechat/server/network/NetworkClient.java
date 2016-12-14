package com.securechat.server.network;

import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.EnumPacketId;
import com.securechat.common.security.RSAEncryption;
import com.securechat.common.security.SecurityUtils;
import com.securechat.server.ChatServer;
import com.securechat.server.UserManager;

public class NetworkClient {
	private ChatServer server;
	private Socket socket;
	private ByteWriter writer;
	private ByteReader reader;
	private ReentrantLock sendLock;
	private Thread readThread;
	private boolean active;
	private RSAEncryption encryption;
	private EnumConnectionStatus status;

	public NetworkClient(ChatServer server, Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;
	}

	public void start() {
		try {
			status = EnumConnectionStatus.PREAUTH;
			PrivateKey key = server.getStore().getKey(ChatServer.netBasePrivateKey, PrivateKey.class);
			encryption = new RSAEncryption(null, key);
			active = true;
			sendLock = new ReentrantLock();
			writer = new ByteWriter(socket.getOutputStream());
			reader = new ByteReader(socket.getInputStream());
			readThread = new Thread(this::readPackets, "ReadThread");
			readThread.start();
		} catch (IOException e) {
			throw new RuntimeException("Failed to connect", e);
		}
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
			EnumPacketId id = data.readEnum(EnumPacketId.class);
			if(status == EnumConnectionStatus.IGNORE){
				System.out.println("Client in ignored state sent an unexpected packet "+id);
			} else if(status == EnumConnectionStatus.PREAUTH){
				if (id == EnumPacketId.REGISTER) {
					handleRegister(data);
				}else{
					System.out.println("Unexpected packet "+id);
				}
			}
		}
	}
	
	private void handleRegister(ByteReader data){
		String username = data.readString();
		PublicKey pubKey = RSAEncryption.loadPublicKey(data.readArray());
		int code = new Random().nextInt();
		encryption.setPublicKey(pubKey);
		System.out.println("Handling register request for " + username);
		
		UserManager um = server.getUserManager();
		if(um.doesUserExist(username)){
			ByteWriter response = new ByteWriter();
			response.writeEnum(EnumPacketId.REGISTER_USERNAME_TAKEN);
			sendPacket(response);
			return;
		}
		
		um.registerUser(username, pubKey, code);

		ByteWriter response = new ByteWriter();
		response.writeEnum(EnumPacketId.REGISTER_SUCCESS);
		response.writeInt(code);
		sendPacket(response);
		status = EnumConnectionStatus.IGNORE;
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
