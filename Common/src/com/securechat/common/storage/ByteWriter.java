package com.securechat.common.storage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.Inject;
import com.securechat.api.common.security.IHasher;
import com.securechat.api.common.storage.IByteWriter;

/**
 * A reference byte writer implementation.
 */
public class ByteWriter implements IByteWriter {
	@Inject
	private IHasher hasher;
	private ByteArrayOutputStream arrayStream;
	private DataOutputStream output;

	@Override
	public void setMemoryOutput() {
		// Allocates an in memory byte bound output stream
		arrayStream = new ByteArrayOutputStream();
		// Wraps stream with a data output stream
		output = new DataOutputStream(arrayStream);
	}

	@Override
	public void setOutput(OutputStream stream) {
		// Wraps stream with a data output stream
		output = new DataOutputStream(stream);
	}

	@Override
	public void writeByte(int i) throws IOException {
		output.writeByte(i);
	}

	@Override
	public void writeEnum(Enum<?> e) throws IOException {
		writeInt(e.ordinal());
	}

	@Override
	public void writeInt(int i) throws IOException {
		output.writeInt(i);
	}

	@Override
	public void writeLong(long l) throws IOException {
		output.writeLong(l);
	}

	@Override
	public void writeString(String str) throws IOException {
		output.writeUTF(str);
	}

	@Override
	public void writeStringWithNull(String str) throws IOException {
		if (str == null) {
			writeBoolean(false);
		} else {
			writeBoolean(true);
			writeString(str);
		}
	}

	@Override
	public void writeBoolean(boolean bool) throws IOException {
		output.writeBoolean(bool);
	}

	@Override
	public void writeArray(byte[] data) throws IOException {
		output.writeInt(data.length);
		output.write(data);
	}

	@Override
	public void writeArrayWithNull(byte[] data) throws IOException {
		if (data == null) {
			writeBoolean(false);
		} else {
			writeBoolean(true);
			writeArray(data);
		}
	}

	@Override
	public void writeFixedArray(byte[] data) throws IOException {
		output.write(data);
	}

	@Override
	public void writeWriterContent(IByteWriter writer) throws IOException {
		writeArray(writer.toByteArray());
	}

	@Override
	public void writeWriterWithChecksum(IByteWriter writer) throws IOException {
		byte[] content = writer.toByteArray();
		writeArray(hasher.hashData(content));
		writeArray(content);
	}

	@Override
	public byte[] toByteArray() {
		return arrayStream.toByteArray();
	}

	@Override
	public void close() {
		try {
			output.close();
			if (arrayStream != null)
				arrayStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker("inbuilt", "n/a", "byte_writer", "1.0.0");
	}
}