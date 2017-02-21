package com.securechat.api.common.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class ObjectDataFormat implements IDataFormat {
	private List<String> names;
	private Map<String, IDataFormat> formats;
	private Map<String, FieldType> types;
	private String primary;

	public ObjectDataFormat() {
		names = new ArrayList<String>();
		formats = new HashMap<String, IDataFormat>();
		types = new HashMap<String, FieldType>();
	}

	public ObjectDataFormat(JSONObject obj) {
		this();
		if (!obj.getString("type").equals("object")) {
			throw new RuntimeException("Not an object");
		}

		JSONArray values = obj.getJSONArray("value");
		for (int i = 0; i < values.length(); i++) {
			JSONObject entry = values.getJSONObject(i);
			String name = entry.getString("name");
			addField(name, IDataFormat.parse(entry.getJSONObject("format")), entry.getEnum(FieldType.class, "type"));
		}
	}

	public ObjectDataInstance create() {
		return new ObjectDataInstance(this);
	}

	public void addField(String name, IDataFormat format, FieldType type) {
		names.add(name);
		formats.put(name, format);
		types.put(name, type);

		if (primary != null && type == FieldType.Primary) {
			throw new RuntimeException("Primary field already exists!");
		} else if (type == FieldType.Primary) {
			primary = name;
		}
	}

	@Override
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", "object");

		JSONArray values = new JSONArray();
		obj.put("value", values);

		for (String name : names) {
			JSONObject entry = new JSONObject();
			entry.put("name", name);
			entry.put("type", types.get(name));
			entry.put("format", formats.get(name).toJSON());
			values.put(entry);
		}

		return obj;
	}
	
	@Override
	public boolean equals(Object a, Object b) {
		return a.equals(b);
	}

	@Override
	public Object save(Object data) {
		return ((ObjectDataInstance) data).toJSON();
	}

	@Override
	public Object load(Object data) {
		return new ObjectDataInstance(this, (JSONObject) data);
	}

	public List<String> getNames() {
		return names;
	}

	public boolean fieldExists(String name) {
		return names.contains(name);
	}

	public IDataFormat getFormat(String name) {
		return formats.get(name);
	}

	public FieldType getType(String name) {
		return types.get(name);
	}

	public String getPrimary() {
		return primary;
	}

	@Override
	public boolean validate(Object value) {
		if (!(value instanceof ObjectDataInstance))
			return false;

		ObjectDataInstance instance = (ObjectDataInstance) value;
		Map<String, Object> values = instance.getValues();

		for (String field : names) {
			IDataFormat format = formats.get(field);
			FieldType type = types.get(field);

			if (!values.containsKey(field)) {
				if (type == FieldType.Optional) {
					continue;
				}
				return false;
			}

			if (!format.validate(values.get(field))) {
				return false;
			}
		}

		for (String field : values.keySet()) {
			if (!names.contains(field)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formats == null) ? 0 : formats.hashCode());
		result = prime * result + ((names == null) ? 0 : names.hashCode());
		result = prime * result + ((primary == null) ? 0 : primary.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
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
		ObjectDataFormat other = (ObjectDataFormat) obj;
		if (formats == null) {
			if (other.formats != null)
				return false;
		} else if (!formats.equals(other.formats))
			return false;
		if (names == null) {
			if (other.names != null)
				return false;
		} else if (!names.equals(other.names))
			return false;
		if (primary == null) {
			if (other.primary != null)
				return false;
		} else if (!primary.equals(other.primary))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		return true;
	}

}