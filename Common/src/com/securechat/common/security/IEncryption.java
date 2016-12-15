package com.securechat.common.security;

public interface IEncryption {

	byte[] encrypt(byte[] data);

	byte[] decrypt(byte[] data);

}
