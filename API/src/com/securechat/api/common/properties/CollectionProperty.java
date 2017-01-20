package com.securechat.api.common.properties;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollectionProperty implements IProperty<PropertyCollection> {
	private String name;
	private IProperty[] defaultProperties;

	public CollectionProperty(String name, IProperty... defaultProperties) {
		this.name = name;
		this.defaultProperties = defaultProperties;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void store(JSONObject obj, PropertyCollection value) {
		Map<IProperty, Object> properties = value.getProperties();

		JSONObject result = new JSONObject(value.getSource().toMap());
		for (Entry<IProperty, Object> property : properties.entrySet()) {
			property.getKey().store(result, property.getValue());
		}

		for (IProperty property : defaultProperties) {
			if (!result.has(property.getName())) {
				property.storeDefault(result);
			}
		}

		obj.put(name, result);
	}

	@Override
	public void storeDefault(JSONObject obj) {
		JSONObject result = new JSONObject();

		for (IProperty property : defaultProperties) {
			property.storeDefault(result);
		}

		obj.put(name, result);
	}

	@Override
	public PropertyCollection getDefault() {
		return new PropertyCollection(null, defaultProperties);
	}

	@Override
	public PropertyCollection load(JSONObject obj) {
		if (!obj.has(name)) {
			storeDefault(obj);
		}

		return new PropertyCollection(obj.getJSONObject(name));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CollectionProperty other = (CollectionProperty) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CollectionProperty [name=" + name + ", defaultProperties=" + Arrays.toString(defaultProperties) + "]";
	}

}
