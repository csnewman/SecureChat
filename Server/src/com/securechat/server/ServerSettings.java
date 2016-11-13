package com.securechat.server;

import java.io.File;

import org.json.JSONObject;

import com.securechat.common.JsonUtil;

public class ServerSettings {
	private static File targetFile = new File("settings.json");
	private String serverName, publicIp;
	private boolean changed;
	private int port;

	public void loadDefaults() {
		serverName = "Unnamed Server";
		port = 1234;
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
}
