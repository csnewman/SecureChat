package com.securechat.api.common.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a collection of values as an object.
 */
public class ObjectDataFormat implements IDataFormat {
	private List<String> names;
	private Map<String, IDataFormat> formats;
	private Map<String, FieldType> types;
	private String primary;

	/**
	 * Generates a new format.
	 */
	public ObjectDataFormat() {
		names = new ArrayList<String>();
		formats = new HashMap<String, IDataFormat>();
		types = new HashMap<String, FieldType>();
	}

	/**
	 * Loads a format from JSON.
	 * 
	 * @param obj
	 *            the JSON data
	 */
	public ObjectDataFormat(JSONObject obj) {
		this();
		// Ensures the JSON object contans the object marker
		if (!obj.getString("type").equals("object")) {
			throw new RuntimeException("Not an object");
		}

		// Loads each value
		JSONArray values = obj.getJSONArray("value");
		for (int i = 0; i < values.length(); i++) {
			JSONObject entry = values.getJSONObject(i);
			String name = entry.getString("name");
			// Sets the field value
			addField(name, IDataFormat.parse(entry.getJSONObject("format")), entry.getEnum(FieldType.class, "type"));
		}
	}

	/**
	 * Creates a new instance with this format.
	 * 
	 * @return the new instance
	 */
	public ObjectDataInstance create() {
		return new ObjectDataInstance(this);
	}

	/**
	 * Adds a new field to the format.
	 * 
	 * @param name
	 *            the field name
	 * @param format
	 *            the field format
	 * @param type
	 *            the field type
	 */
	public void addField(String name, IDataFormat format, FieldType type) {
		names.add(name);
		formats.put(name, format);
		types.put(name, type);

		// Ensure a primary field does not already exist
		if (primary != null && type == FieldType.Primary) {
			throw new RuntimeException("Primary field already exists!");
		} else if (type == FieldType.Primary) {
			primary = name;
		}
	}

	/**
	 * Converts the format to JSON.
	 */
	@Override
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		// Adds object marker
		obj.put("type", "object");

		JSONArray values = new JSONArray();
		obj.put("value", values);

		// Stores each field value
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

	/**
	 * Gets all the fields names.
	 * 
	 * @return all field names
	 */
	public List<String> getNames() {
		return names;
	}

	/**
	 * Checks whether a field exists.
	 * 
	 * @param name
	 *            the field name
	 * @return whether the field exists
	 */
	public boolean fieldExists(String name) {
		return names.contains(name);
	}

	/**
	 * Gets the format of a field.
	 * 
	 * @param name
	 *            the field name
	 * @return the field format
	 */
	public IDataFormat getFormat(String name) {
		return formats.get(name);
	}

	/**
	 * Gets the type of a field.
	 * 
	 * @param name
	 *            the field name
	 * @return the field type
	 */
	public FieldType getType(String name) {
		return types.get(name);
	}

	/**
	 * Returns the name of the primary field.
	 * 
	 * @return the field name
	 */
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

			// Checks that the required fields exist
			if (!values.containsKey(field)) {
				if (type == FieldType.Optional) {
					continue;
				}
				return false;
			}

			// Ensures the field is of the correct format
			if (!format.validate(values.get(field))) {
				return false;
			}
		}

		// Checks that no extra fields exists
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