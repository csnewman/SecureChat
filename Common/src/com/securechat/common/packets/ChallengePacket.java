package com.securechat.common.packets;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public class ChallengePacket implements IPacket {
	private int tempCode;

	public ChallengePacket() {
	}

	public ChallengePacket(int tempCode) {
		this.tempCode = tempCode;
	}

	@Override
	public void read(ByteReader reader) {
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
