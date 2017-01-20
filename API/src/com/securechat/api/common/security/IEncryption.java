package com.securechat.api.common.security;

import java.io.IOException;

import com.securechat.api.common.implementation.IImplementation;

public interface IEncryption extends IImplementation {

	byte[] encrypt(byte[] data) throws IOException;

	byte[] decrypt(byte[] data) throws IOException;

}
