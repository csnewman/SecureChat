package com.securechat.plugins.basicconnectionsprofiles;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;

/**
 * A reference implementation of a connection profile.
 */
public class BasicConnectionProfile implements IConnectionProfile {
	private boolean isTemplate;
	private String name, username, ip;
	private int port, authcode;
	private byte[] publicKey, privateKey;

	public BasicConnectionProfile(boolean isTemplate, String name, String username, String ip, int port, int authcode,
			byte[] publicKey, byte[] privateKey) {
		this.isTemplate = isTemplate;
		this.name = name;
		this.username = username;
		this.ip = ip;
		this.port = port;
		this.authcode = authcode;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	@Override
	public boolean isTemplate() {
		return isTemplate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getIP() {
		return ip;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public int getAuthCode() {
		return authcode;
	}

	@Override
	public byte[] getPublicKey() {
		return publicKey;
	}

	@Override
	public byte[] getPrivateKey() {
		return privateKey;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker(BasicConnectionProfilesPlugin.NAME, BasicConnectionProfilesPlugin.VERSION,
				"connection_profile", "1.0.0");
	}

}
