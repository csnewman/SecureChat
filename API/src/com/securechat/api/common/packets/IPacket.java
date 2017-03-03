package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

/**
 * Represents any message that can be sent over the network.
 */
public interface IPacket {

	/**
	 * Reads the content of the packet from the given data.
	 * 
	 * @param reader
	 *            the data to read
	 * @throws IOException
	 */
	void read(IByteReader reader) throws IOException;

	/**
	 * Writes the content of the packet.
	 * 
	 * @param writer
	 * @throws IOException
	 */
	void write(IByteWriter writer) throws IOException;

}
