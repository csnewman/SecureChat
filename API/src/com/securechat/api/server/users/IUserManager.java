package com.securechat.api.server.users;

import com.securechat.api.common.implementation.IImplementation;

public interface IUserManager extends IImplementation {
	
	public void init();
	
	public boolean doesUserExist(String username);
	
	public void registerUser(String username, byte[] publicKey, int clientCode);
	
	public IUser getUser(String username);
	
	public String[] getAllUsernames();
	
}
