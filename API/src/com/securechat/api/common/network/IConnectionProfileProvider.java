package com.securechat.api.common.network;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.security.IEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;

public interface IConnectionProfileProvider extends IImplementation {
	
	public IConnectionProfile generateProfileTemplate(String name, String ip, int port, byte[] publicKey);
	
	public IConnectionProfile createProfile(IConnectionProfile template, String username, int authcode, byte[] privateKey);
	
	public IConnectionProfile loadProfileFromFile(IStorage storage, String path, IEncryption encryption);
	
	public IConnectionProfile loadProfileFromMemory(IByteReader reader, IEncryption encryption);
	
	public void saveProfileToFIle(IConnectionProfile profile, IStorage storage, String path, IEncryption encryption);
	
	public void saveProfileToMemory(IConnectionProfile profile, IByteWriter writer, IEncryption encryption);
	
	public String getDisplayName();
	
}
