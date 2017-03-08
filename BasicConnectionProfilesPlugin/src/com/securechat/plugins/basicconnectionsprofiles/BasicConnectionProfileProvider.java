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

/**
 * A reference implementation of a profile provider.
 */
public class BasicConnectionProfileProvider implements IConnectionProfileProvider {
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
	public IConnectionProfile loadProfileFromFile(IStorage storage, String path, IEncryption encryption)
			throws IOException {
		return loadProfileFromMemory(storage.readFile(path, encryption), null);
	}

	@Override
	public IConnectionProfile loadProfileFromMemory(IByteReader reader, IEncryption encryption) throws IOException {
		if (encryption != null)
			reader = IByteReader.get(factory, getMarker().getId(), encryption.decrypt(reader.getRawData()));

		return new BasicConnectionProfile(reader.readBoolean(), reader.readStringWithNull(),
				reader.readStringWithNull(), reader.readStringWithNull(), reader.readInt(), reader.readInt(),
				reader.readArrayWithNull(), reader.readArrayWithNull());
	}

	@Override
	public void saveProfileToFIle(IConnectionProfile profile, IStorage storage, String path, IEncryption encryption)
			throws IOException {
		IByteWriter writer = IByteWriter.get(factory, getMarker().getId());
		saveProfileToMemory(profile, writer, null);
		storage.writeFile(path, encryption, writer);
	}

	@Override
	public void saveProfileToMemory(IConnectionProfile profile, IByteWriter writer, IEncryption encryption)
			throws IOException {
		IByteWriter out;
		if (encryption != null) {
			out = IByteWriter.get(factory, getMarker().getId());
		} else {
			out = writer;
		}

		out.writeBoolean(profile.isTemplate());
		out.writeStringWithNull(profile.getName());
		out.writeStringWithNull(profile.getUsername());
		out.writeStringWithNull(profile.getIP());
		out.writeInt(profile.getPort());
		out.writeInt(profile.getAuthCode());
		out.writeArrayWithNull(profile.getPublicKey());
		out.writeArrayWithNull(profile.getPrivateKey());

		if (encryption != null) {
			try {
				writer.writeArray(encryption.encrypt(out.toByteArray()));
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

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker(BasicConnectionProfilesPlugin.NAME, BasicConnectionProfilesPlugin.VERSION,
				"connection_profile_provider", "1.0.0");
	}

}
