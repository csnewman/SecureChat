package com.securechat.api.common.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public class ObjectDataInstance {
	private ObjectDataFormat format;
	private Map<String, Object> values;

	public ObjectDataInstance(ObjectDataFormat format) {
		this.format = format;
		values = new HashMap<String, Object>();
	}

	public ObjectDataInstance(ObjectDataFormat format, JSONObject obj) {
		this(format);

		for (String name : format.getNames()) {
			values.put(name, format.getFormat(name).load(obj.get(name)));
		}
	}

	public boolean matches(ObjectDataInstance data) {
		for (Entry<String, Object> val : values.entrySet()) {
			String name = val.getKey();
			if (data.hasField(name)) {
				if (!format.getFormat(name).equals(data.getField(name), getField(name))) {
					return false;
				}
			}
		}
		return true;
	}

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();

		for (Entry<String, Object> entry : values.entrySet()) {
			obj.put(entry.getKey(), format.getFormat(entry.getKey()).save(entry.getValue()));
		}

		return obj;
	}

	public void setField(String name, Object value) {
		if (!format.fieldExists(name)) {
			throw new RuntimeException("Field " + name + " does not exist!");
		}
		values.put(name, value);
	}

	public Object getField(String name) {
		if (!format.fieldExists(name)) {
			throw new RuntimeException("Field " + name + " does not exist!");
		}
		return values.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getField(String name, Class<T> clazz) {
		return (T) getField(name);
	}

	public boolean hasField(String name) {
		return values.containsKey(name);
	}

	public boolean validate() {
		return format.validate(this);
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public Object getPrimary() {
		return getField(format.getPrimary());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		ObjectDataInstance other = (ObjectDataInstance) obj;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

}
