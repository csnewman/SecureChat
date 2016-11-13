package com.securechat.common.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public abstract class ProtectedStore {
	private static byte[] headerPrefix = new byte[] { 'S', 'C', 'P', 'S', 0X56, 0X1A, 0X11 };
	private File file;
	private String passwordHash;

	public ProtectedStore(File file, String passwordHash) {
		this.file = file;
		this.passwordHash = passwordHash;
	}

	public void load() {
		if (file.exists()) {
			byte[] rawData;
			try {
				rawData = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				throw new RuntimeException("Failed to read store", e);
			}

			// TODO: Password

			ByteReader headerReader = new ByteReader(rawData);

			byte[] foundPefix = headerReader.readFixedArray(headerPrefix.length);
			if (!Arrays.equals(headerPrefix, foundPefix)) {
				throw new RuntimeException("Incorrect header prefix found!");
			}

			byte[] foundChecksum = headerReader.readArray();
			byte[] content = headerReader.readArray();
			byte[] checksum = SecurityUtils.hashData(content);

			if (!Arrays.equals(checksum, foundChecksum)) {
				throw new RuntimeException("Invalid checksum!");
			}

			ByteReader bodyReader = new ByteReader(content);
			loadContent(bodyReader);
			bodyReader.close();
			headerReader.close();
		}
	}

	protected abstract void loadContent(ByteReader bodyReader);

	public void save() {
		ByteWriter bodyWriter = new ByteWriter();
		writeContent(bodyWriter);

		ByteWriter headerWriter = new ByteWriter();
		headerWriter.writeFixedArray(headerPrefix);
		byte[] content = bodyWriter.toByteArray();
		headerWriter.writeArray(SecurityUtils.hashData(content));
		headerWriter.writeArray(content);

		// TODO: Password

		try {
			Files.write(file.toPath(), headerWriter.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Failed to write store", e);
		}

		bodyWriter.close();
		headerWriter.close();
	}

	protected abstract void writeContent(ByteWriter writer);
	
	public boolean exists(){
		return file.exists();
	}
}
