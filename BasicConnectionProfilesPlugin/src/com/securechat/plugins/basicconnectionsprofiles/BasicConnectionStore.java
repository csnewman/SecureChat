package com.securechat.plugins.basicconnectionsprofiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.client.network.IConnectionStoreUpdateListener;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;

public class BasicConnectionStore implements IConnectionStore {
	public static final ImplementationMarker MARKER = new ImplementationMarker(BasicConnectionProfilesPlugin.NAME,
			BasicConnectionProfilesPlugin.VERSION, "connection_store", "1.0.0");
	private static final String path = "connections.bin";
	@InjectInstance
	private IKeystore keystore;
	@InjectInstance
	private IStorage storage;
	@InjectInstance
	private IImplementationFactory factory;
	private List<IConnectionStoreUpdateListener> listeners;
	private List<IConnectionProfile> profiles;
	private IAsymmetricKeyEncryption key;

	public BasicConnectionStore() {
		listeners = new ArrayList<IConnectionStoreUpdateListener>();
		profiles = new ArrayList<IConnectionProfile>();
	}

	@Override
	public void init() {
		key = factory.provide(IAsymmetricKeyEncryption.class, null, true, true, MARKER.getId());
		keystore.loadAsymmetricKeyOrGenerate(MARKER.getId(), key);

		if (storage.doesFileExist(path)) {
			IByteReader fileData = storage.readFile(path, key);
			try {
				IByteReader content = fileData.readReaderWithChecksum();
				int size = content.readInt();
				for (int i = 0; i < size; i++) {
					profiles.add(new BasicConnectionProfile(content.readBoolean(), content.readStringWithNull(),
							content.readStringWithNull(), content.readStringWithNull(), content.readInt(),
							content.readInt(), content.readArrayWithNull(), content.readArrayWithNull()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void save() {
		IByteWriter body = IByteWriter.get(factory, MARKER.getId());

		body.writeInt(profiles.size());
		for (IConnectionProfile profile : profiles) {
			body.writeBoolean(profile.isTemplate());
			body.writeStringWithNull(profile.getName());
			body.writeStringWithNull(profile.getUsername());
			body.writeStringWithNull(profile.getIP());
			body.writeInt(profile.getPort());
			body.writeInt(profile.getAuthCode());
			body.writeArrayWithNull(profile.getPublicKey());
			body.writeArrayWithNull(profile.getPrivateKey());
		}

		IByteWriter finalData = IByteWriter.get(factory, MARKER.getId());
		finalData.writeWriterWithChecksum(body);
		storage.writeFile(path, key, finalData);
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
		save();
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
