package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by the server to inform the client of the users on the server and
 * whether they are online.
 */
public class UserListPacket implements IPacket {
	private String[] usernames;
	private boolean[] online;

	public UserListPacket() {
	}

	public UserListPacket(String[] usernames, boolean[] online) {
		this.usernames = usernames;
		this.online = online;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		int size = reader.readInt();
		usernames = new String[size];
		online = new boolean[size];
		for (int i = 0; i < size; i++) {
			usernames[i] = reader.readString();
			online[i] = reader.readBoolean();
		}
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeInt(usernames.length);
		for (int i = 0; i < usernames.length; i++) {
			writer.writeString(usernames[i]);
			writer.writeBoolean(online[i]);
		}
	}

	public String[] getUsernames() {
		return usernames;
	}

	public boolean[] getOnline() {
		return online;
	}

}