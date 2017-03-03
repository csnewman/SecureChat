package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by either end informing them that they should close the connection for
 * the given reason.
 */
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
	public void write(IByteWriter writer) throws IOException {
		writer.writeString(reason);
	}

	public String getReason() {
		return reason;
	}

}
