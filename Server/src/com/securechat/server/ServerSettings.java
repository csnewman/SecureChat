package com.securechat.server;

import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PrimitiveProperty;
import com.securechat.api.common.properties.PropertyCollection;

public class ServerSettings {
	private static final PrimitiveProperty<String> NAME_PROPERY = new PrimitiveProperty<String>("name",
			"Unnamed Server");
	private static final PrimitiveProperty<String> IP_PROPERY = new PrimitiveProperty<String>("public_ip", "localhost");
	private static final PrimitiveProperty<Integer> PORT_PROPERY = new PrimitiveProperty<Integer>("public_port", 1234);
	private static final CollectionProperty NETWORK_PROPERTY = new CollectionProperty("network", IP_PROPERY,
			PORT_PROPERY);

	private static final PrimitiveProperty<Boolean> GENERATE_PROPERY = new PrimitiveProperty<Boolean>("generate", true);
	private static final PrimitiveProperty<String> PASSWORD_PROPERY = new PrimitiveProperty<String>("password",
			"unset");
	private static final CollectionProperty PROFILE_PROPERTY = new CollectionProperty("profile", GENERATE_PROPERY,
			PASSWORD_PROPERY);

	private static final CollectionProperty SERVER_PROPERTY = new CollectionProperty("server", NAME_PROPERY,
			NETWORK_PROPERTY);

	private String serverName, publicIp, profilePassword;
	private boolean generateProfile;
	private int publicPort;

	public ServerSettings(PropertyCollection collection) {
		PropertyCollection server = collection.getPermissive(SERVER_PROPERTY);
		serverName = server.get(NAME_PROPERY);

		PropertyCollection network = server.getPermissive(NETWORK_PROPERTY);
		publicIp = network.get(IP_PROPERY);
		publicPort = network.get(PORT_PROPERY);

		PropertyCollection profile = server.getPermissive(PROFILE_PROPERTY);
		generateProfile = profile.get(GENERATE_PROPERY);
		profilePassword = profile.get(PASSWORD_PROPERY);
	}

	public String getServerName() {
		return serverName;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public int getPublicPort() {
		return publicPort;
	}

	public boolean shouldGenerateProfile() {
		return generateProfile;
	}

	public String getProfilePassword() {
		return profilePassword;
	}

}
