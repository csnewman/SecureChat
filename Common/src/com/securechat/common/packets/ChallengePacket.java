package com.securechat.common.packets;

import java.io.IOException;

import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;

public class ChallengePacket implements IPacket {
	private int tempCode;

	public ChallengePacket() {
	}

	public ChallengePacket(int tempCode) {
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
