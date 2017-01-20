package com.securechat.common.storage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.securechat.api.common.storage.IByteWriter;
import com.securechat.common.security.SecurityUtils;

public class ByteWriter implements IByteWriter {
	private ByteArrayOutputStream arrayStream;
	private DataOutputStream output;

	@Override
	public void setMemoryOutput() {
		arrayStream = new ByteArrayOutputStream();
		output = new DataOutputStream(arrayStream);
	}

	@Override
	public void setStream(OutputStream stream) {
		output = new DataOutputStream(stream);
	}
	
	@Override
	public void writeByte(int i) {
		try {
			output.writeByte(i);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	@Override
	public void writeEnum(Enum<?> e) {
		writeInt(e.ordinal());
	}

	@Override
	public void writeInt(int i) {
		try {
			output.writeInt(i);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	@Override
	public void writeString(String str) {
		try {
			output.writeUTF(str);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	@Override
	public void writeBoolean(boolean bool) {
		try {
			output.writeBoolean(bool);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	@Override
	public void writeArray(byte[] data) {
		try {
			output.writeInt(data.length);
			output.write(data);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	@Override
	public void writeFixedArray(byte[] data) {
		try {
			output.write(data);
		} catch (IOException e) {
			throw new RuntimeException("Internal error occured", e);
		}
	}

	@Override
	public void writeWriterContent(IByteWriter writer) {
		writeArray(writer.toByteArray());
	}

	@Override
	public void writeWriterWithChecksum(IByteWriter writer) {
		byte[] content = writer.toByteArray();
		writeArray(SecurityUtils.hashData(content));
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
	public String getImplName() {
		return "official-byte_writer";
	}

}