package com.securechat.api.common.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.securechat.api.common.storage.IStorage;

/**
 * Stores a collection of named properties.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PropertyCollection {
	private Map<IProperty, Object> properties;
	private JSONObject source;
	private List<IProperty> defaultProperties;

	/**
	 * Creates a new empty collection
	 */
	public PropertyCollection() {
		this(null);
	}

	/**
	 * Creates a new collection with a JSON object to load from with default
	 * properties.
	 * 
	 * @param source
	 *            the json to load from
	 * @param defaultProperties
	 *            the default properties
	 */
	public PropertyCollection(JSONObject source, IProperty... defaultProperties) {
		this.source = source != null ? new JSONObject(source.toMap()) : new JSONObject();
		properties = new HashMap<IProperty, Object>();
		this.defaultProperties = Arrays.asList(defaultProperties);
	}

	/**
	 * Stores the content of the collection.
	 * 
	 * @param storage
	 *            the storage to store it in
	 * @param path
	 *            the path to store it at
	 */
	public void saveToFile(IStorage storage, String path) {
		JSONObject result = new JSONObject(source.toMap());
		for (Entry<IProperty, Object> property : properties.entrySet()) {
			property.getKey().store(result, property.getValue());
		}

		for (IProperty property : defaultProperties) {
			if (!result.has(property.getName())) {
				property.storeDefault(result);
			}
		}

		storage.writeJsonFile(path, result);
	}

	/**
	 * Loads the collection from a file.
	 * 
	 * @param storage
	 *            the storage to load it from
	 * @param path
	 *            the path to load from
	 */
	public void loadFile(IStorage storage, String path) {
		properties.clear();
		source = storage.readJsonFile(path);
	}

	/**
	 * Checks whether a property exists.
	 * 
	 * @param property
	 *            the property to check for
	 * @return whether the property exists
	 */
	public <T> boolean exists(IProperty<T> property) {
		return properties.containsKey(property) || source.has(property.getName());
	}

	/**
	 * Gets the value of the property if it exists otherwise it stores the
	 * default value and returns it.
	 * 
	 * @param property
	 *            the property to fetch
	 * @return the value of the property
	 */
	public <T> T getPermissive(IProperty<T> property) {
		T val = get(property);
		if (val == null) {
			val = property.getDefault();
			set(property, val);
		}
		return val;
	}

	/**
	 * Gets the value of the property if it exists.
	 * 
	 * @param property
	 *            the property to fetch
	 * @return the value of the property
	 */
	public <T> T get(IProperty<T> property) {
		if (!properties.containsKey(property)) {
			if (source.has(property.getName())) {
				set(property, property.load(source));
				source.remove(property.getName());
			} else if (defaultProperties.contains(property)) {
				set(property, property.getDefault());
			} else {
				return null;
			}
		}
		return (T) properties.get(property);
	}

	/**
	 * Sets the value of a property.
	 * 
	 * @param property
	 *            the property to set
	 * @param value
	 *            the value to set the property to
	 */
	public <T> void set(IProperty<T> property, T value) {
		properties.put(property, value);
	}

	/**
	 * Adds a default property.
	 * 
	 * @param property
	 *            the property to add.
	 */
	public void addProperty(IProperty property) {
		defaultProperties.add(property);
	}

	/**
	 * Returns all properties in this collection.
	 * 
	 * @return all the properties
	 */
	public Map<IProperty, Object> getProperties() {
		return properties;
	}

	public JSONObject getSource() {
		return source;
	}

}
