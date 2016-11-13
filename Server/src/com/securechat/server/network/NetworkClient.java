package com.securechat.server.network;

import java.io.IOException;
import java.net.Socket;

import com.securechat.common.ByteWriter;

public class NetworkClient {
	private Socket socket;
	private ByteWriter writer;
	private Thread thread;
	
	public NetworkClient(Socket socket) throws IOException {
		this.socket = socket;
		writer = new ByteWriter(socket.getOutputStream());
	}
	
	public void start(){
		ByteWriter handshake = new ByteWriter();
		handshake.writeString("Hello world!");
		sendPacket(handshake);		
	}
	
	public void sendPacket(ByteWriter packet){
		writer.writeWriterContent(packet);
	}
	
	
	
	
}
