package com.securechat.api.common.implementation;

import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PrimitiveProperty;
import com.securechat.api.common.properties.PropertyCollection;

public class ImplementationMarker {
	private static final PrimitiveProperty<String> PLUGIN_NAME_PROPERTY = new PrimitiveProperty<String>("plugin-name");
	private static final PrimitiveProperty<String> PLUGIN_VERSION_PROPERTY = new PrimitiveProperty<String>(
			"plugin-version");
	private static final PrimitiveProperty<String> NAME_PROPERTY = new PrimitiveProperty<String>("name");
	private static final PrimitiveProperty<String> VERSION_PROPERTY = new PrimitiveProperty<String>("version");
	private String pluginName, pluginVersion, name, version;

	public ImplementationMarker(String id) {
		String[] parts = id.split(":");
		if (parts.length != 4) {
			throw new RuntimeException("Invalid id format! " + id);
		}
		pluginName = parts[0];
		pluginVersion = parts[1];
		name = parts[2];
		version = parts[3];
	}

	public ImplementationMarker(String pluginName, String pluginVersion, String name, String version) {
		this.pluginName = pluginName;
		this.pluginVersion = pluginVersion;
		this.name = name;
		this.version = version;
	}

	public PropertyCollection save() {
		PropertyCollection collection = new PropertyCollection();
		save(collection);
		return collection;
	}

	public void save(PropertyCollection collection) {
		collection.set(PLUGIN_NAME_PROPERTY, pluginName);
		collection.set(PLUGIN_VERSION_PROPERTY, pluginVersion);
		collection.set(NAME_PROPERTY, name);
		collection.set(VERSION_PROPERTY, version);
	}

	public String getId() {
		return pluginName + ":" + pluginVersion + ":" + name + ":" + version;
	}

	public String getPluginName() {
		return pluginName;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public static ImplementationMarker loadMarker(PropertyCollection parent, String name) {
		return loadMarker(parent.get(new CollectionProperty(name)));
	}

	public static ImplementationMarker loadMarker(PropertyCollection collection) {
		if (collection == null)
			return null;

		String pluginName = collection.get(PLUGIN_NAME_PROPERTY);
		String pluginVersion = collection.get(PLUGIN_VERSION_PROPERTY);
		String name = collection.get(NAME_PROPERTY);
		String version = collection.get(VERSION_PROPERTY);
		return new ImplementationMarker(pluginName, pluginVersion, name, version);
	}

	public static ImplementationMarker[] convert(String[] ids) {
		ImplementationMarker[] markers = new ImplementationMarker[ids.length];

		for (int i = 0; i < ids.length; i++) {
			markers[i] = new ImplementationMarker(ids[i]);
		}

		return markers;
	}

	@Override
	public String toString() {
		return "ImplementationMarker [pluginName=" + pluginName + ", pluginVersion=" + pluginVersion + ", name=" + name
				+ ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pluginName == null) ? 0 : pluginName.hashCode());
		result = prime * result + ((pluginVersion == null) ? 0 : pluginVersion.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImplementationMarker other = (ImplementationMarker) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pluginName == null) {
			if (other.pluginName != null)
				return false;
		} else if (!pluginName.equals(other.pluginName))
			return false;
		if (pluginVersion == null) {
			if (other.pluginVersion != null)
				return false;
		} else if (!pluginVersion.equals(other.pluginVersion))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
