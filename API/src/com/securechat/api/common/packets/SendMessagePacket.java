package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class SendMessagePacket implements IPacket {
	private String id;
	private byte[] content;
	private long time;

	public SendMessagePacket() {
	}

	public SendMessagePacket(String id, byte[] content, long time) {
		this.id = id;
		this.content = content;
		this.time = time;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		id = reader.readString();
		content = reader.readArray();
		time = reader.readLong();
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeString(id);
		writer.writeArray(content);
		writer.writeLong(time);
	}

	public String getId() {
		return id;
	}

	public byte[] getContent() {
		return content;
	}

	public long getTime() {
		return time;
	}

}
