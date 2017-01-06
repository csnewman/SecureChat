package com.securechat.common.api;

import java.io.IOException;

public interface IEncryption extends IImplementation {

	byte[] encrypt(byte[] data) throws IOException;

	byte[] decrypt(byte[] data) throws IOException;

}
