package com.securechat.api.common.storage;

import java.io.IOException;
import java.io.InputStream;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;

public interface IByteReader extends IImplementation {

	public void setMemoryInput(byte[] data);

	public void setInput(InputStream stream);

	public byte[] getRawData();

	public int readByte() throws IOException;

	public <T extends Enum<T>> T readEnum(Class<T> type) throws IOException;

	public int readInt() throws IOException;

	public String readString() throws IOException;

	public boolean readBoolean() throws IOException;

	public byte[] readArray() throws IOException;

	public byte[] readFixedArray(int size) throws IOException;

	public IByteReader readReaderContent() throws IOException;

	public IByteReader readReaderWithChecksum() throws IOException;

	public int getSize();

	public void close() throws IOException;

	public static IByteReader get(IImplementationFactory factory, String name, byte[] data){
		IByteReader reader = factory.provide(IByteReader.class, new String[0], true, true, name);
		reader.setMemoryInput(data);
		return reader;
	}
	
}
