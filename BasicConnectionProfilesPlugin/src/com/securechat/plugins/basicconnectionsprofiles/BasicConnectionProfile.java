package com.securechat.plugins.basicconnectionsprofiles;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;

public class BasicConnectionProfile implements IConnectionProfile {
	public static final ImplementationMarker MARKER = new ImplementationMarker(BasicConnectionProfilesPlugin.NAME,
			BasicConnectionProfilesPlugin.VERSION, "connection_profile", "1.0.0");
	private boolean isTemplate;
	private String name, username, ip;
	private int port, authcode;

	public BasicConnectionProfile(boolean isTemplate, String name, String username, String ip, int port, int authcode) {
		this.isTemplate = isTemplate;
		this.name = name;
		this.username = username;
		this.ip = ip;
		this.port = port;
		this.authcode = authcode;
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
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
