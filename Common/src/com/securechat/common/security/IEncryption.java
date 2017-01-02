package com.securechat.common.security;

import java.io.IOException;

public interface IEncryption {

	byte[] encrypt(byte[] data);

	byte[] decrypt(byte[] data) throws IOException;

}
