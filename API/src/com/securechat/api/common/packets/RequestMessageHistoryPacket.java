package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by the client to the server to request missing chat history.
 */
public class RequestMessageHistoryPacket implements IPacket {
	private String cid;
	private int lastId;

	public RequestMessageHistoryPacket() {
	}

	public RequestMessageHistoryPacket(String cid, int lastId) {
		this.cid = cid;
		this.lastId = lastId;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		cid = reader.readString();
		lastId = reader.readInt();
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeString(cid);
		writer.writeInt(lastId);
	}

	public String getChatId() {
		return cid;
	}

	public int getLastId() {
		return lastId;
	}

}
