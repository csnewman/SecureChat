package com.securechat.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetworkServer {
	private int port;
	private boolean active;
	private ServerSocket serverSocket;
	private Thread listenThread;
	private List<NetworkClient> clients;
	private ReentrantReadWriteLock clientLock;
	
	public NetworkServer(int port) {
		this.port = port;
	}
	
	public void start(){
		try {
			active = true;
			clientLock = new ReentrantReadWriteLock();
			clients = new ArrayList<NetworkClient>();
			serverSocket = new ServerSocket(port);
			listenThread = new Thread(this::handleConnections, "Listen Thread");
			listenThread.setDaemon(false);
			listenThread.start();
			System.out.println("Started network server!");
		} catch (IOException e) {
			throw new RuntimeException("Failed to start network server!", e);
		}
	}
	
	private void handleConnections(){
		while (active){
			try {
				Socket socket = serverSocket.accept();
				if(socket != null){
					System.out.println("Client connected!");
					NetworkClient client = new NetworkClient(socket);
					clientLock.writeLock().lock();
					clients.add(client);
					clientLock.writeLock().unlock();
					client.start();
				}
			} catch (IOException e) {
				System.out.println("Connection failed! "+e.getMessage());
			}
		}
	}
	
	public void close(){
		try{
			active = false;
			serverSocket.close();
			listenThread.interrupt();
		}catch (IOException e) {
			throw new RuntimeException("Failed to stop network server!", e);
		}
	}
}
