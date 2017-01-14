package com.securechat.common;

import java.util.List;

import com.securechat.common.security.IEncryption;

public class FileStorage implements IStorage{
//	private File base
	
	@Override
	public List<Class<?>> getPlugins() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doesFileExist(String path) {
		return false;
	}

	@Override
	public ByteReader readFile(String path, IEncryption encryption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeFile(String path, IEncryption encryption, ByteWriter data) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getImplName() {
		return "official-file_storage";
	}

}
