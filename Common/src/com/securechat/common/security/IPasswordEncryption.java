package com.securechat.common.security;

public interface IPasswordEncryption extends IEncryption{
	
	public void init(char[] password);
	
}
