package com.securechat.api.server.network;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;

public interface IServerNetworkManager extends IImplementation {

	public void init(IAsymmetricKeyEncryption networkKey);

	public IConnectionProfile generateProfile(IConnectionProfileProvider provider);

	public void start();

	public void stop();

}
