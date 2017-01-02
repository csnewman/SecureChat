package com.securechat.common.packets;

import java.io.IOException;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public interface IPacket {

	void read(ByteReader reader) throws IOException;

	void write(ByteWriter writer);

}
