package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class DisconnectPacket implements IPacket {
	private String reason;

	public DisconnectPacket() {
	}

	public DisconnectPacket(String reason) {
		this.reason = reason;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		reason = reader.readString();
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeString(reason);
	}

	public String getReason() {
		return reason;
	}

}
