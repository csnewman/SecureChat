package com.securechat.common.storage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.Inject;
import com.securechat.api.common.security.IHasher;
import com.securechat.api.common.storage.IByteReader;

/**
 * A reference byte reader implementation.
 */
public class ByteReader implements IByteReader {
	@Inject
	private IHasher hasher;
	private byte[] rawData;
	private ByteArrayInputStream arrayStream;
	private DataInputStream input;

	@Override
	public void setMemoryInput(byte[] data) {
		rawData = data;
		// Wraps access to the byte array in an input stream
		arrayStream = new ByteArrayInputStream(data);
		// Wraps stream with a data input stream
		input = new DataInputStream(arrayStream);
	}

	@Override
	public void setInput(InputStream stream) {
		// Wraps stream with a data input stream
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
	public long readLong() throws IOException {
		return input.readLong();
	}

	@Override
	public String readString() throws IOException {
		return input.readUTF();
	}

	@Override
	public String readStringWithNull() throws IOException {
		if (readBoolean()) {
			return readString();
		}
		return null;
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
	public byte[] readArrayWithNull() throws IOException {
		if (readBoolean()) {
			return readArray();
		}
		return null;
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
		// Reads the checksum and data
		byte[] foundChecksum = readArray();
		byte[] content = readArray();

		// calculates checksum of read data
		byte[] checksum = hasher.hashData(content);

		// Ensures the checksums are equal
		if (!Arrays.equals(checksum, foundChecksum)) {
			throw new IOException("Invalid checksum!");
		}

		// Wraps content in reader
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

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker("inbuilt", "n/a", "byte_reader", "1.0.0");
	}

}