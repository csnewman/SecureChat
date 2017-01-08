package com.securechat.common.security;

import java.io.IOException;

import com.securechat.common.implementation.IImplementation;

public interface IEncryption extends IImplementation {

	byte[] encrypt(byte[] data) throws IOException;

	byte[] decrypt(byte[] data) throws IOException;

}
