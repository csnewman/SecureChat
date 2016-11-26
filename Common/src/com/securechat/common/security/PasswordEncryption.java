package com.securechat.common.security;

public class PasswordEncryption implements IEncryption {
	private String passwordHash;

	public PasswordEncryption(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public byte[] encrypt(byte[] data) {
		return data;
	}

	@Override
	public byte[] decrypt(byte[] data) {
		return data;
	}

}
