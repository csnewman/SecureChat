package com.securechat.api.server.users;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.INetworkConnection;

public interface IUser extends IImplementation {
	
	public String getUsername();

	public byte[] getPublicKey();
	
	public int getClientCode();

	public void assignToConnection(INetworkConnection connection);
	
}
