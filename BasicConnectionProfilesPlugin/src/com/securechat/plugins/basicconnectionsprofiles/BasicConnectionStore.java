package com.securechat.plugins.basicconnectionsprofiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.client.network.IConnectionStoreUpdateListener;
import com.securechat.api.common.IContext;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;

/**
 * A reference implementation of a connection store.
 */
public class BasicConnectionStore implements IConnectionStore {
	@InjectInstance
	private IContext context;
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
		try {
			// Fetches a key from the key store
			key = factory.provide(IAsymmetricKeyEncryption.class, null, true, true, MARKER.getId());
			keystore.loadAsymmetricKeyOrGenerate(MARKER.getId(), key);

			if (storage.doesFileExist(PATH)) {
				IByteReader fileData = storage.readFile(PATH, key);
				try {
					IByteReader content = fileData.readReaderWithChecksum();

					// Loads each connection from the file
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
		} catch (IOException e) {
			context.handleCrash(e);
		}
	}

	public void save() {
		try {
			IByteWriter body = IByteWriter.get(factory, MARKER.getId());

			// Writes each connection to the file
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

			// Saves the file with the encryption key
			IByteWriter finalData = IByteWriter.get(factory, MARKER.getId());
			finalData.writeWriterWithChecksum(body);
			storage.writeFile(PATH, key, finalData);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static final ImplementationMarker MARKER;
	private static final String PATH;
	static {
		MARKER = new ImplementationMarker(BasicConnectionProfilesPlugin.NAME, BasicConnectionProfilesPlugin.VERSION,
				"connection_store", "1.0.0");
		PATH = "connections.bin";
	}

}
