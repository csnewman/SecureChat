package com.securechat.api.common.packets;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Sent by the server to inform the client that they have successfully logged
 * in.
 */
public class ConnectedPacket implements IPacket {

	@Override
	public void read(IByteReader reader) {
	}

	@Override
	public void write(IByteWriter writer) {
	}

}
