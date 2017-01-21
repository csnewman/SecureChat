package com.securechat.api.common.packets;

import java.io.IOException;

import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class RegisterResponsePacket implements IPacket {
	private RegisterStatus status;
	private int code;

	public RegisterResponsePacket() {
	}

	public RegisterResponsePacket(RegisterStatus status) {
		this(status, -1);
	}

	public RegisterResponsePacket(int code) {
		this(RegisterStatus.Success, code);
	}

	public RegisterResponsePacket(RegisterStatus status, int code) {
		this.status = status;
		this.code = code;
	}

	@Override
	public void read(IByteReader reader) throws IOException {
		status = reader.readEnum(RegisterStatus.class);
		code = reader.readInt();
	}

	@Override
	public void write(IByteWriter writer) {
		writer.writeEnum(status);
		writer.writeInt(code);
	}

	public RegisterStatus getStatus() {
		return status;
	}

	public int getCode() {
		return code;
	}

	public static enum RegisterStatus {
		Success, UsernameTaken
	}

}
