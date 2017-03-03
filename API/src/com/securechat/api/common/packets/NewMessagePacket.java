package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by the server to inform the client of a new message.
 */
public class NewMessagePacket implements IPacket {
	private String cid, sender;
	private int mid;
	private byte[] content;
	private long time;

	public NewMessagePacket() {
	}

	public NewMessagePacket(String cid, String sender, int mid, byte[] content, long time) {
		this.cid = cid;
		this.sender = sender;
		this.mid = mid;
		this.content = content;
		this.time = time;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		cid = reader.readString();
		sender = reader.readString();
		mid = reader.readInt();
		content = reader.readArray();
		time = reader.readLong();
	}

	@Override
	public void write(IByteWriter writer) throws IOException{
		writer.writeString(cid);
		writer.writeString(sender);
		writer.writeInt(mid);
		writer.writeArray(content);
		writer.writeLong(time);
	}

	public String getChatId() {
		return cid;
	}
	
	public int getMessageId() {
		return mid;
	}
	
	public String getSender() {
		return sender;
	}

	public byte[] getContent() {
		return content;
	}

	public long getTime() {
		return time;
	}

}
