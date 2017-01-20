package com.securechat.common.packets;

import java.io.IOException;

import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;

public class ChallengeResponsePacket implements IPacket {
	private int tempCode;

	public ChallengeResponsePacket() {
	}

	public ChallengeResponsePacket(int tempCode) {
		this.tempCode = tempCode;
	}

	@Override
	public void read(ByteReader reader) throws IOException {
		tempCode = reader.readInt();
	}

	@Override
	public void write(ByteWriter writer) {
		writer.writeInt(tempCode);
	}

	public int getTempCode() {
		return tempCode;
	}

}
