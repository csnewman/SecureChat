package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent back in response to a challenge packet with the same temp code for the
 * server to check.
 */
public class ChallengeResponsePacket implements IPacket {
	private int tempCode;

	public ChallengeResponsePacket() {
	}

	public ChallengeResponsePacket(int tempCode) {
		this.tempCode = tempCode;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		tempCode = reader.readInt();
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeInt(tempCode);
	}

	public int getTempCode() {
		return tempCode;
	}

}
