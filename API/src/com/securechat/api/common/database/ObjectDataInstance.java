package com.securechat.api.common.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

/**
 * An instance of an ObjectDataFormat with values for each field.
 */
public class ObjectDataInstance {
	private ObjectDataFormat format;
	private Map<String, Object> values;

	/**
	 * Creates a new instance with the given format.
	 * 
	 * @param format
	 *            the format
	 */
	public ObjectDataInstance(ObjectDataFormat format) {
		this.format = format;
		values = new HashMap<String, Object>();
	}

	/**
	 * Loads the values from the JSON object with the given format.
	 * 
	 * @param format
	 *            the format
	 * @param obj
	 *            the JSON object
	 */
	public ObjectDataInstance(ObjectDataFormat format, JSONObject obj) {
		this(format);

		for (String name : format.getNames()) {
			values.put(name, format.getFormat(name).load(obj.get(name)));
		}
	}

	/**
	 * Checks whether the fields that are set in both have the same values.
	 * 
	 * @param data
	 *            the other instance
	 * @return whether they match
	 */
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

	/**
	 * Stores the values into a JSON object.
	 * 
	 * @return the JSON output
	 */
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();

		for (Entry<String, Object> entry : values.entrySet()) {
			obj.put(entry.getKey(), format.getFormat(entry.getKey()).save(entry.getValue()));
		}

		return obj;
	}

	/**
	 * Sets the value of a field
	 * 
	 * @param name
	 *            the field name
	 * @param value
	 *            the field value
	 */
	public void setField(String name, Object value) {
		if (!format.fieldExists(name)) {
			throw new RuntimeException("Field " + name + " does not exist!");
		}
		values.put(name, value);
	}

	/**
	 * Gets the value of a field
	 * 
	 * @param name
	 *            the field name
	 * @return the field value
	 */
	public Object getField(String name) {
		if (!format.fieldExists(name)) {
			throw new RuntimeException("Field " + name + " does not exist!");
		}
		return values.get(name);
	}

	/**
	 * Gets the value of a field and casts it to the given type.
	 * 
	 * @param name
	 *            the field name
	 * @param clazz
	 *            the target type
	 * @return the field value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getField(String name, Class<T> clazz) {
		return (T) getField(name);
	}

	/**
	 * Checks whether the field contains a value for a field.
	 * 
	 * @param name
	 *            the field name
	 * @return whether a value exists
	 */
	public boolean hasField(String name) {
		return values.containsKey(name);
	}

	/**
	 * Validates this instance against its format.
	 * 
	 * @return whether it is valid
	 */
	public boolean validate() {
		return format.validate(this);
	}

	/**
	 * Returns a map of all keys and values
	 * @return
	 */
	public Map<String, Object> getValues() {
		return values;
	}

	/**
	 * Gets the primary key value
	 * 
	 * @return the primary key value
	 */
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
