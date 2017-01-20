package com.securechat.api.common.storage;

import java.util.List;

import org.json.JSONObject;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.security.IEncryption;

public interface IStorage extends IImplementation {

	public void init();
	
	public List<String> loadPlugins();
	
	public boolean doesFileExist(String path);
	
	public IByteReader readFile(String path, IEncryption encryption);
	
	public JSONObject readJsonFile(String path);
	
	public void writeFile(String path, IEncryption encryption, IByteWriter data);
	
	public void writeJsonFile(String path, JSONObject obj);
	
}
