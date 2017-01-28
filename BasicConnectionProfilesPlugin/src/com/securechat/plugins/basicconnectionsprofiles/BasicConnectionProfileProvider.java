package com.securechat.plugins.basicconnectionsprofiles;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.implementation.NotImplementedException;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.security.IEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IFileSystem;

public class BasicConnectionProfileProvider implements IConnectionProfileProvider {
	public static final ImplementationMarker MARKER = new ImplementationMarker(BasicConnectionProfilesPlugin.NAME,
			BasicConnectionProfilesPlugin.VERSION, "connection_profile_provider", "1.0.0");

	@Override
	public IConnectionProfile generateProfileTemplate(String name, String ip, int port) {
		return new BasicConnectionProfile(true, name, null, ip, port, -1);
	}

	@Override
	public IConnectionProfile createProfile(IConnectionProfile template, String username, int authcode) {
		return new BasicConnectionProfile(false, template.getName(), username, template.getIP(), template.getPort(),
				authcode);
	}

	@Override
	public IConnectionProfile loadProfileFromFile(IFileSystem fileSysem, String path, IEncryption encryption) {
		throw new NotImplementedException();
	}

	@Override
	public IConnectionProfile loadProfileFromMemory(IByteReader reader, IEncryption encryption) {
		throw new NotImplementedException();
	}

	@Override
	public void saveProfileToFIle(IConnectionProfile profile, IFileSystem fileSystem, String path,
			IEncryption encryption) {
		throw new NotImplementedException();
	}

	@Override
	public void saveProfileToMemory(IConnectionProfile profile, IByteWriter writer, IEncryption encryption) {
		throw new NotImplementedException();
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
