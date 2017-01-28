package com.securechat.plugins.basicconnectionsprofiles;

import java.util.ArrayList;
import java.util.List;

import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.client.network.IConnectionStoreUpdateListener;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;

public class BasicConnectionStore implements IConnectionStore {
	public static final ImplementationMarker MARKER = new ImplementationMarker(BasicConnectionProfilesPlugin.NAME,
			BasicConnectionProfilesPlugin.VERSION, "connection_store", "1.0.0");
	private List<IConnectionStoreUpdateListener> listeners;
	private List<IConnectionProfile> profiles;

	public BasicConnectionStore() {
		listeners = new ArrayList<IConnectionStoreUpdateListener>();
		profiles = new ArrayList<IConnectionProfile>();
	}

	@Override
	public void load() {
	}
	
	@Override
	public void addUpdateListener(IConnectionStoreUpdateListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeUpdateListener(IConnectionStoreUpdateListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void addProfile(IConnectionProfile profile) {
		profiles.add(profile);
		for (IConnectionStoreUpdateListener listener : listeners) {
			listener.onConnectionStoreUpdated();
		}
	}

	@Override
	public List<IConnectionProfile> getProfiles() {
		return profiles;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
