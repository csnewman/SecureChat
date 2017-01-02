package com.securechat.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteReader {
	private byte[] rawData;
	private ByteArrayInputStream arrayStream;
	private DataInputStream input;

	public ByteReader(byte[] data) {
		rawData = data;
		arrayStream = new ByteArrayInputStream(data);
		input = new DataInputStream(arrayStream);
	}

	public ByteReader(InputStream stream) {
		input = new DataInputStream(stream);
	}

	public byte[] getRawData() {
		return rawData;
	}

	public int readByte() throws IOException {
		return input.read();
	}

	public <T extends Enum<T>> T readEnum(Class<T> type) throws IOException {
		return type.getEnumConstants()[readInt()];
	}

	public int readInt() throws IOException {
		return input.readInt();
	}

	public String readString() throws IOException {
		return input.readUTF();
	}

	public boolean readBoolean() throws IOException {
		return input.readBoolean();
	}

	public byte[] readArray() throws IOException {
		int size = input.readInt();
		byte[] buffer = new byte[size];
		input.readFully(buffer, 0, size);
		return buffer;
	}

	public byte[] readFixedArray(int size) throws IOException {
		byte[] buffer = new byte[size];
		input.readFully(buffer, 0, size);
		return buffer;
	}

	public ByteReader readReaderContent() throws IOException {
		return new ByteReader(readArray());
	}

	public int getSize() {
		try {
			return arrayStream != null ? arrayStream.available() : input.available();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public void close() throws IOException {
		input.close();
		if (arrayStream != null)
			arrayStream.close();
	}

}