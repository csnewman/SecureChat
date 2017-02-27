package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by a server to give the client missing chat messages.
 */
public class MessageHistoryPacket implements IPacket {
	private String cid;
	private int lastId;
	private String[] senders;
	private long[] times;
	private byte[][] contents;

	public MessageHistoryPacket() {
	}

	public MessageHistoryPacket(String cid, int lastId, String[] senders, long[] times, byte[][] contents) {
		this.cid = cid;
		this.lastId = lastId;
		this.senders = senders;
		this.times = times;
		this.contents = contents;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		cid = reader.readString();
		lastId = reader.readInt();
		int size = reader.readInt();
		senders = new String[size];
		times = new long[size];
		contents = new byte[size][];
		for (int i = 0; i < size; i++) {
			senders[i] = reader.readString();
			times[i] = reader.readLong();
			contents[i] = reader.readArray();
		}
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeString(cid);
		writer.writeInt(lastId);
		writer.writeInt(senders.length);
		for (int i = 0; i < senders.length; i++) {
			writer.writeString(senders[i]);
			writer.writeLong(times[i]);
			writer.writeArray(contents[i]);
		}
	}

	public String getChatId() {
		return cid;
	}

	public int getLastId() {
		return lastId;
	}

	public String[] getSenders() {
		return senders;
	}

	public long[] getTimes() {
		return times;
	}

	public byte[][] getContents() {
		return contents;
	}

}
