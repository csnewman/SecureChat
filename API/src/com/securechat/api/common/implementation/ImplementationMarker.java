package com.securechat.api.common.implementation;

import com.securechat.api.common.properties.PrimitiveProperty;
import com.securechat.api.common.properties.PropertyCollection;

/**
 * The ID of an implementation.
 */
public class ImplementationMarker {
	private String pluginName, pluginVersion, name, version;

	/**
	 * Loads an implementation marker from a colon separated string.
	 * 
	 * @param id
	 *            the colon seperated string
	 */
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

	/**
	 * Creates a marker from the given information.
	 * 
	 * @param pluginName
	 *            the name of the plugin that owns this implementation
	 * @param pluginVersion
	 *            the version of the plugin that owns this implementation
	 * @param name
	 *            the name of this implementation
	 * @param version
	 *            the version of this implementation
	 */
	public ImplementationMarker(String pluginName, String pluginVersion, String name, String version) {
		this.pluginName = pluginName;
		this.pluginVersion = pluginVersion;
		this.name = name;
		this.version = version;
	}

	/**
	 * Converts this marker into a property collection.
	 * 
	 * @return the property collection
	 */
	public PropertyCollection toJSON() {
		PropertyCollection collection = new PropertyCollection();
		toJSON(collection);
		return collection;
	}

	/**
	 * Sets the markers information into the given collection.
	 * 
	 * @param collection
	 *            the collection to update
	 */
	public void toJSON(PropertyCollection collection) {
		collection.set(PLUGIN_NAME_PROPERTY, pluginName);
		collection.set(PLUGIN_VERSION_PROPERTY, pluginVersion);
		collection.set(NAME_PROPERTY, name);
		collection.set(VERSION_PROPERTY, version);
	}

	/**
	 * Returns the colon separated id string.
	 * 
	 * @return the colon separated id string
	 */
	public String getId() {
		return pluginName + ":" + pluginVersion + ":" + name + ":" + version;
	}

	/**
	 * Gets the owning plugins name.
	 * 
	 * @return the plugins name
	 */
	public String getPluginName() {
		return pluginName;
	}

	/**
	 * Gets the owning plugins version.
	 * 
	 * @return the plugins version
	 */
	public String getPluginVersion() {
		return pluginVersion;
	}

	/**
	 * Gets the implementation name.
	 * 
	 * @return the implementation name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the implementations version.
	 * 
	 * @return the implementation version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Loads a marker from the property collection
	 * 
	 * @param collection
	 *            the collection to load from
	 * @return the loaded version
	 */
	public static ImplementationMarker loadMarker(PropertyCollection collection) {
		if (collection == null)
			return null;

		String pluginName = collection.get(PLUGIN_NAME_PROPERTY);
		String pluginVersion = collection.get(PLUGIN_VERSION_PROPERTY);
		String name = collection.get(NAME_PROPERTY);
		String version = collection.get(VERSION_PROPERTY);
		return new ImplementationMarker(pluginName, pluginVersion, name, version);
	}

	/**
	 * Converts the array of implementation ids into an array of markers.
	 * 
	 * @param ids
	 *            the ids to convert
	 * @return the converted markers
	 */
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

	private static final PrimitiveProperty<String> PLUGIN_NAME_PROPERTY, PLUGIN_VERSION_PROPERTY, NAME_PROPERTY,
			VERSION_PROPERTY;
	static {
		PLUGIN_NAME_PROPERTY = new PrimitiveProperty<String>("plugin-name");
		PLUGIN_VERSION_PROPERTY = new PrimitiveProperty<String>("plugin-version");
		NAME_PROPERTY = new PrimitiveProperty<String>("name");
		VERSION_PROPERTY = new PrimitiveProperty<String>("version");
	}

}
