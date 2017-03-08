package com.securechat.common.plugins;

import com.securechat.api.common.Sides;
import com.securechat.api.common.plugins.Plugin;

/**
 * An instance of a plugin.
 */
public class PluginInstance {
	private Object instance;
	private String name, version;
	private Sides side;
	private Class<?> clazz;

	public PluginInstance(Plugin info, Class<?> clazz) {
		this.name = info.name();
		this.version = info.version();
		this.side = info.side();
		this.clazz = clazz;
	}

	public boolean createInstance() {
		try {
			instance = clazz.newInstance();
			return true;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Object getInstance() {
		return instance;
	}

	public String getFullString() {
		return clazz.getName() + "(name=" + name + ", version=" + version + ", side=" + side + ")";
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public Sides getSide() {
		return side;
	}

}
