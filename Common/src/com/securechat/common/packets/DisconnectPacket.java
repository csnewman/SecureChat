package com.securechat.common.packets;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public class DisconnectPacket implements IPacket {
	private String reason;

	public DisconnectPacket() {
	}

	public DisconnectPacket(String reason) {
		this.reason = reason;
	}

	@Override
	public void read(ByteReader reader) {
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
