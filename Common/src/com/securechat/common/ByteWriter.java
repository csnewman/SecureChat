package com.securechat.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteWriter {
	private ByteArrayOutputStream arrayStream;
	private DataOutputStream output;

	public ByteWriter() {
		arrayStream = new ByteArrayOutputStream();
		output = new DataOutputStream(arrayStream);
	}

	public ByteWriter(OutputStream stream) {
		output = new DataOutputStream(stream);
	}

	public void writeInt(int i) {
		try {
			output.writeInt(i);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public void writeString(String str) {
		try {
			output.writeUTF(str);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public void writeBoolean(boolean bool) {
		try {
			output.writeBoolean(bool);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public void writeArray(byte[] data) {
		try {
			output.writeInt(data.length);
			output.write(data);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}
	
	public void writeFixedArray(byte[] data) {
		try {
			output.write(data);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	public void writeWriterContent(ByteWriter writer) {
		writeArray(writer.toByteArray());
	}

	public byte[] toByteArray() {
		return arrayStream.toByteArray();
	}

	public void close() {
		try {
			output.close();
			if (arrayStream != null)
				arrayStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

}