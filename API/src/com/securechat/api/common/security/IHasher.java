package com.securechat.api.common.security;

import com.securechat.api.common.implementation.IImplementation;

public interface IHasher extends IImplementation {

	public byte[] hashData(byte[] input);

	public char[] hashChars(char[] chars);

}
