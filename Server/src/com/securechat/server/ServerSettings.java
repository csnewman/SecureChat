package com.securechat.server;

import java.io.File;

import org.json.JSONObject;

import com.securechat.common.JsonUtil;

public class ServerSettings {
	private static File targetFile = new File("settings.json");
	private String serverName, publicIp, connectionInfoPassword;
	private boolean changed, generateConnectionInfo;
	private int port;

	public void loadDefaults() {
		serverName = "Unnamed Server";
		publicIp = "127.0.0.1";
		port = 1234;
		connectionInfoPassword = "!!! ENTER YOUR PASSWORD HERE !!!";
	}

	public void load() {
		changed = false;
		loadDefaults();

		if (targetFile.exists()) {
			JSONObject file = JsonUtil.parseFile(targetFile);
			serverName = JsonUtil.getOrDefault(file, "serverName", serverName, String.class);

			if (file.has("network")) {
				JSONObject netConf = file.getJSONObject("network");
				publicIp = JsonUtil.getOrDefault(netConf, "publicIP", publicIp, String.class);
				port = JsonUtil.getOrDefault(netConf, "port", port, int.class);
			}

			if (file.has("connectionInfo")) {
				JSONObject conInfo = file.getJSONObject("connectionInfo");
				generateConnectionInfo = JsonUtil.getOrDefault(conInfo, "generate", generateConnectionInfo,
						boolean.class);
				connectionInfoPassword = JsonUtil.getOrDefault(conInfo, "password", connectionInfoPassword,
						String.class);
			}
		} else {
			save();
		}
	}

	public void save() {
		changed = false;
		JSONObject file = new JSONObject();
		file.put("serverName", serverName);

		JSONObject netConf = new JSONObject();
		netConf.put("publicIP", publicIp);
		netConf.put("port", port);
		file.put("network", netConf);

		JSONObject conInfo = new JSONObject();
		conInfo.put("generate", generateConnectionInfo);
		conInfo.put("password", connectionInfoPassword);
		file.put("connectionInfo", conInfo);

		JsonUtil.writeFile(targetFile, file);
	}

	public boolean hasChanged() {
		return changed;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		changed = true;
		this.serverName = serverName;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		changed = true;
		this.publicIp = publicIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
		changed = true;
	}

	public boolean shouldGenerateConnectionInfo() {
		return generateConnectionInfo;
	}

	public void setGenerateConnectionInfo(boolean generateConnectionInfo) {
		this.generateConnectionInfo = generateConnectionInfo;
	}

	public String getConnectionInfoPassword() {
		return connectionInfoPassword;
	}

	public void setConnectionInfoPassword(String connectionInfoPassword) {
		this.connectionInfoPassword = connectionInfoPassword;
	}
}
