package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public interface IPacket {

	void read(IByteReader reader) throws IOException;

	void write(IByteWriter writer);

}
