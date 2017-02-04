package com.securechat.api.common.network;

import com.securechat.api.common.implementation.IImplementation;

public interface IConnectionProfile extends IImplementation {
	
	public boolean isTemplate();
	
	public String getName();
	
	public String getUsername();
	
	public String getIP();
	
	public int getPort();
	
	public int getAuthCode();
	
	public byte[] getPublicKey();
	
	public byte[] getPrivateKey();
	
}
