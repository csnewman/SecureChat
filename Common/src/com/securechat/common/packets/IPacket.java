package com.securechat.common.packets;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public interface IPacket {

	void read(ByteReader reader);

	void write(ByteWriter writer);

}
