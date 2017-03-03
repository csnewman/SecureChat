package com.securechat.api.common.storage;

import java.io.IOException;
import java.io.OutputStream;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;

/**
 * Writes objects directly to an input stream.
 */
public interface IByteWriter extends IImplementation {
	
	public void setMemoryOutput();

	public void setOutput(OutputStream stream);

	public void writeByte(int i) throws IOException;

	public void writeEnum(Enum<?> e) throws IOException;

	public void writeInt(int i) throws IOException;

	public void writeLong(long l) throws IOException;

	public void writeString(String str) throws IOException;

	public void writeStringWithNull(String str) throws IOException;

	public void writeBoolean(boolean bool) throws IOException;

	public void writeArray(byte[] data) throws IOException;

	public void writeArrayWithNull(byte[] data) throws IOException;

	public void writeFixedArray(byte[] data) throws IOException;

	public void writeWriterContent(IByteWriter writer) throws IOException;

	public void writeWriterWithChecksum(IByteWriter writer) throws IOException;

	public byte[] toByteArray();

	public void close();

	public static IByteWriter get(IImplementationFactory factory, String name) {
		IByteWriter writer = factory.provide(IByteWriter.class, new ImplementationMarker[0], true, true, name);
		writer.setMemoryOutput();
		return writer;
	}

	public static IByteWriter get(IImplementationFactory factory, String name, OutputStream stream) {
		IByteWriter writer = factory.provide(IByteWriter.class, new ImplementationMarker[0], true, true, name);
		writer.setOutput(stream);
		return writer;
	}

}
