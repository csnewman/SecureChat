package com.securechat.common.packets;

import java.io.IOException;

import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;

public class DisconnectPacket implements IPacket {
	private String reason;

	public DisconnectPacket() {
	}

	public DisconnectPacket(String reason) {
		this.reason = reason;
	}

	@Override
	public void read(ByteReader reader) throws IOException {
		reason = reader.readString();
	}

	@Override
	public void write(ByteWriter writer) {
		writer.writeString(reason);
	}

	public String getReason() {
		return reason;
	}

}
