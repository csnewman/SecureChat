package com.securechat.common;

import java.util.List;

import com.securechat.common.implementation.IImplementation;
import com.securechat.common.security.IEncryption;

public interface IStorage extends IImplementation {

	public List<Class<?>> getPlugins();
	
	public boolean doesFileExist(String path);
	
	public ByteReader readFile(String path, IEncryption encryption);
	
	public void writeFile(String path, IEncryption encryption, ByteWriter data);
	
}
