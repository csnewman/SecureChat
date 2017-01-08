package com.securechat.common.properties;

import org.json.JSONObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PrimitiveProperty<T> implements IProperty<T> {
	private String name;
	private T defaultVal;

	public PrimitiveProperty(String name, T defaultVal) {
		this.name = name;
		this.defaultVal = defaultVal;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void store(JSONObject obj, T value) {
		obj.put(name, value);
	}

	@Override
	public T load(JSONObject obj) {
		if (!obj.has(name))
			storeDefault(obj);
		return (T) obj.get(name);
	}

	@Override
	public void storeDefault(JSONObject obj) {
		obj.put(name, defaultVal);
	}

	@Override
	public T getDefault() {
		return defaultVal;
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
		PrimitiveProperty other = (PrimitiveProperty) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}