package com.securechat.api.client.network;

import java.util.List;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.network.IConnectionProfile;

public interface IConnectionStore extends IImplementation {

	public void load();

	public void addUpdateListener(IConnectionStoreUpdateListener listener);
	
	public void removeUpdateListener(IConnectionStoreUpdateListener listener);

	public void addProfile(IConnectionProfile profile);

	public List<IConnectionProfile> getProfiles();

}
