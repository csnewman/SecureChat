package com.securechat.api.server.users;

import com.securechat.api.common.implementation.IImplementation;

public interface IUser extends IImplementation {
	
	public String getUsername();

	public byte[] getPublicKey();
	
	public int getClientCode();
	
}
