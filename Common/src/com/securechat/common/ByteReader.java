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

	public int readByte() {
		try {
			return input.read();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public <T extends Enum<T>> T readEnum(Class<T> type) {
		return type.getEnumConstants()[readInt()];
	}

	public int readInt() {
		try {
			return input.readInt();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public String readString() {
		try {
			return input.readUTF();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public boolean readBoolean() {
		try {
			return input.readBoolean();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public byte[] readArray() {
		try {
			int size = input.readInt();
			byte[] buffer = new byte[size];
			input.readFully(buffer, 0, size);
			return buffer;
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public byte[] readFixedArray(int size) {
		try {
			byte[] buffer = new byte[size];
			input.readFully(buffer, 0, size);
			return buffer;
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public ByteReader readReaderContent() {
		return new ByteReader(readArray());
	}

	public int getSize() {
		try {
			return arrayStream != null ? arrayStream.available() : input.available();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public void close() {
		try {
			input.close();
			if (arrayStream != null)
				arrayStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

}