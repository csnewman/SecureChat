package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by the server to ensure that the client owns the private key associated
 * with the public key on the server.
 */
public class ChallengePacket implements IPacket {
	private int tempCode;

	public ChallengePacket() {
	}

	public ChallengePacket(int tempCode) {
		this.tempCode = tempCode;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		tempCode = reader.readInt();
	}

	@Override
	public void write(IByteWriter writer) throws IOException {
		writer.writeInt(tempCode);
	}

	public int getTempCode() {
		return tempCode;
	}

}
