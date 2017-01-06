package com.securechat.server.network;

import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import com.securechat.basicencryption.RSAEncryption;
import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.packets.ChallengePacket;
import com.securechat.common.packets.ChallengeResponsePacket;
import com.securechat.common.packets.ConnectPacket;
import com.securechat.common.packets.DisconnectPacket;
import com.securechat.common.packets.IPacket;
import com.securechat.common.packets.PacketManager;
import com.securechat.common.packets.RegisterPacket;
import com.securechat.common.packets.RegisterResponsePacket;
import com.securechat.common.packets.RegisterResponsePacket.RegisterStatus;
import com.securechat.common.security.SecurityUtils;
import com.securechat.server.ChatServer;
import com.securechat.server.User;
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
	private EnumConnectionState status;
	private int tempCode;

	public NetworkClient(ChatServer server, Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;
	}

	public void start() {
		try {
			status = EnumConnectionState.PreAuth;
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

				if (status == EnumConnectionState.Ignore) {
					System.out.println("Client in ignored state sent an unexpected packet " + id);
				} else if (status == EnumConnectionState.PreAuth) {
					if (packet instanceof RegisterPacket) {
						handleRegister((RegisterPacket) packet);
					} else if (packet instanceof ConnectPacket) {
						handleConnect((ConnectPacket) packet);
					} else {
						System.out.println("Unexpected packet " + id);
					}
				} else if (status == EnumConnectionState.AwaitingChallengeResponse) {
					if (packet instanceof ChallengeResponsePacket) {
						handleChallengeResponse((ChallengeResponsePacket) packet);
					} else {
						System.out.println("Unexpected packet " + id);
					}
				} else if (status != EnumConnectionState.Connected) {
					user.handlePacket(packet);
				}
			}
		} catch (Exception e) {
			if(active){
				e.printStackTrace();
				System.out.println("Disconnected from client");
			}
		}

	}

	private void handleRegister(RegisterPacket packet) {
		int code = new Random().nextInt();
		encryption.setPublicKey(packet.getPublicKey());
		System.out.println("Handling register request for " + packet.getUsername());

		UserManager um = server.getUserManager();
		if (um.doesUserExist(packet.getUsername())) {
			sendPacket(new RegisterResponsePacket(RegisterStatus.UsernameTaken));
			return;
		}

		um.registerUser(packet.getUsername(), packet.getPublicKey(), code);
		sendPacket(new RegisterResponsePacket(code));
		status = EnumConnectionState.Ignore;
	}

	private User user;

	private void handleConnect(ConnectPacket packet) {
		UserManager um = server.getUserManager();

		if (!um.doesUserExist(packet.getUsername())) {
			System.out.println("Client tried to login as an unknown username");
			disconnect("Unknown username");
			return;
		}

		user = um.getUser(packet.getUsername());
		if (packet.getCode() != user.getCode()) {
			System.out.println("[SECURITY] Client sent wrong code!");
			user = null;
			disconnect("Wrong code");
			return;
		}

		encryption.setPublicKey(user.getPublicKey());

		tempCode = new Random().nextInt();
		status = EnumConnectionState.AwaitingChallengeResponse;
		sendPacket(new ChallengePacket(tempCode));
	}

	private void handleChallengeResponse(ChallengeResponsePacket packet) {
		if (packet.getTempCode() != tempCode) {
			System.out.println("[SECURITY] Client sent wrong temp code back!");
			disconnect("Wrong temp code");
			return;
		}

		status = EnumConnectionState.Connected;
		System.out.println("Client logged in as " + user.getUsername());
		user.assignToNetwork(this);
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

	public void disconnect(String reason) {
		try {
			System.out.println("Disconnecting client: " + reason);
			status = EnumConnectionState.Ignore;
			if (status == EnumConnectionState.Connected) {
				sendPacket(new DisconnectPacket(reason));
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RSAEncryption getEncryption() {
		return encryption;
	}

}
