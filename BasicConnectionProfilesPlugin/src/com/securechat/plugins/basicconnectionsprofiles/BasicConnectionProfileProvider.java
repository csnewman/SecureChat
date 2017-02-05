package com.securechat.plugins.basicconnectionsprofiles;

import java.io.IOException;

import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;

public class BasicConnectionProfileProvider implements IConnectionProfileProvider {
	public static final ImplementationMarker MARKER = new ImplementationMarker(BasicConnectionProfilesPlugin.NAME,
			BasicConnectionProfilesPlugin.VERSION, "connection_profile_provider", "1.0.0");
	@InjectInstance
	private IImplementationFactory factory;

	@Override
	public IConnectionProfile generateProfileTemplate(String name, String ip, int port, byte[] publicKey) {
		return new BasicConnectionProfile(true, name, null, ip, port, -1, publicKey, null);
	}

	@Override
	public IConnectionProfile createProfile(IConnectionProfile template, String username, int authcode,
			byte[] privateKey) {
		return new BasicConnectionProfile(false, template.getName(), username, template.getIP(), template.getPort(),
				authcode, template.getPublicKey(), privateKey);
	}

	@Override
	public IConnectionProfile loadProfileFromFile(IStorage storage, String path, IEncryption encryption) {
		return loadProfileFromMemory(storage.readFile(path, encryption), null);
	}

	@Override
	public IConnectionProfile loadProfileFromMemory(IByteReader reader, IEncryption encryption) {
		try {
			if (encryption != null)
				reader = IByteReader.get(factory, getMarker().getId(), encryption.decrypt(reader.getRawData()));

			return new BasicConnectionProfile(reader.readBoolean(), reader.readStringWithNull(),
					reader.readStringWithNull(), reader.readStringWithNull(), reader.readInt(), reader.readInt(),
					reader.readArrayWithNull(), reader.readArrayWithNull());

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void saveProfileToFIle(IConnectionProfile profile, IStorage storage, String path, IEncryption encryption) {
		IByteWriter writer = IByteWriter.get(factory, getMarker().getId());
		saveProfileToMemory(profile, writer, null);
		storage.writeFile(path, encryption, writer);
	}

	@Override
	public void saveProfileToMemory(IConnectionProfile profile, IByteWriter writer, IEncryption encryption) {
		IByteWriter temp;
		if (encryption != null) {
			temp = IByteWriter.get(factory, getMarker().getId());
		} else {
			temp = writer;
		}

		temp.writeBoolean(profile.isTemplate());
		temp.writeStringWithNull(profile.getName());
		temp.writeStringWithNull(profile.getUsername());
		temp.writeStringWithNull(profile.getIP());
		temp.writeInt(profile.getPort());
		temp.writeInt(profile.getAuthCode());
		temp.writeArrayWithNull(profile.getPublicKey());
		temp.writeArrayWithNull(profile.getPrivateKey());

		if (encryption != null) {
			try {
				writer.writeArray(encryption.encrypt(temp.toByteArray()));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public String getDisplayName() {
		return "BCPF (official)";
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
