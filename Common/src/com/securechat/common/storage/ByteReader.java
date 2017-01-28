package com.securechat.common.storage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.common.security.SecurityUtils;

public class ByteReader implements IByteReader {
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "byte_reader",
			"1.0.0");
	private byte[] rawData;
	private ByteArrayInputStream arrayStream;
	private DataInputStream input;

	@Override
	public void setMemoryInput(byte[] data) {
		rawData = data;
		arrayStream = new ByteArrayInputStream(data);
		input = new DataInputStream(arrayStream);
	}

	@Override
	public void setInput(InputStream stream) {
		input = new DataInputStream(stream);
	}

	@Override
	public byte[] getRawData() {
		return rawData;
	}

	@Override
	public int readByte() throws IOException {
		return input.read();
	}

	@Override
	public <T extends Enum<T>> T readEnum(Class<T> type) throws IOException {
		return type.getEnumConstants()[readInt()];
	}

	@Override
	public int readInt() throws IOException {
		return input.readInt();
	}

	@Override
	public String readString() throws IOException {
		return input.readUTF();
	}

	@Override
	public boolean readBoolean() throws IOException {
		return input.readBoolean();
	}

	@Override
	public byte[] readArray() throws IOException {
		int size = input.readInt();
		byte[] buffer = new byte[size];
		input.readFully(buffer, 0, size);
		return buffer;
	}

	@Override
	public byte[] readFixedArray(int size) throws IOException {
		byte[] buffer = new byte[size];
		input.readFully(buffer, 0, size);
		return buffer;
	}

	@Override
	public IByteReader readReaderContent() throws IOException {
		ByteReader reader = new ByteReader();
		reader.setMemoryInput(readArray());
		return reader;
	}

	@Override
	public IByteReader readReaderWithChecksum() throws IOException {
		byte[] foundChecksum = readArray();
		byte[] content = readArray();
		byte[] checksum = SecurityUtils.hashData(content);

		if (!Arrays.equals(checksum, foundChecksum)) {
			throw new IOException("Invalid checksum!");
		}

		ByteReader reader = new ByteReader();
		reader.setMemoryInput(content);
		return reader;
	}

	@Override
	public int getSize() {
		try {
			return arrayStream != null ? arrayStream.available() : input.available();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	@Override
	public void close() throws IOException {
		input.close();
		if (arrayStream != null)
			arrayStream.close();
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}