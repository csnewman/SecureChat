package com.securechat.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.packets.IPacket;
import com.securechat.api.common.packets.UserListPacket;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.server.IServerManager;
import com.securechat.api.server.users.IUser;
import com.securechat.api.server.users.IUserManager;

public class ServerManager implements IServerManager {
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "server_manager",
			"1.0.0");
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IUserManager userManager;
	private Map<String, IUser> onlineUsers;
	private ReentrantLock lock;

	@Override
	public void init() {
		onlineUsers = new HashMap<String, IUser>();
		lock = new ReentrantLock();
	}

	@Override
	public void handleUserLogin(IUser user) {
		log.info("User logged in");
		lock.lock();
		onlineUsers.put(user.getUsername(), user);
		updateUserList();
		lock.unlock();
		user.sendChatList();
	}

	@Override
	public void handleUserLost(IUser user) {
		log.info("User lost");
		lock.lock();
		onlineUsers.remove(user.getUsername());
		updateUserList();
		lock.unlock();
	}

	private void updateUserList() {
		log.debug("Sending user list");
		String[] users = userManager.getAllUsernames();
		boolean[] online = new boolean[users.length];

		for (int i = 0; i < users.length; i++) {
			online[i] = onlineUsers.containsKey(users[i]);
		}

		sendPacketToAll(new UserListPacket(users, online));
	}
	
	@Override
	public boolean isUserOnline(String username) {
		return onlineUsers.containsKey(username);
	}

	@Override
	public IUser getOnlineUser(String username) {
		return onlineUsers.get(username);
	}

	private void sendPacketToAll(IPacket packet) {
		log.debug("Sending packet to all "+packet);
		for (IUser user : onlineUsers.values()) {
			user.sendPacket(packet);
		}
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
