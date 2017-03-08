package com.securechat.api.common.database;

import org.json.JSONObject;

/**
 * Stores the format of a type
 */
public interface IDataFormat {

	/**
	 * Checks whether the given object is compatible with the format.
	 * 
	 * @param value
	 *            the object to check
	 * @return whether the object is compatible
	 */
	boolean validate(Object value);

	/**
	 * Returns the JSON marker for this format.
	 * 
	 * @return the JSON marker
	 */
	JSONObject toJSON();

	/**
	 * Converts the input into a JSON compatible format.
	 * 
	 * @param data
	 *            the object to convert
	 * @return the JSON compatible output
	 */
	Object save(Object data);

	/**
	 * Converts the JSON compatible format back to the original format.
	 * 
	 * @param data
	 *            the JSON compatible input
	 * @return the original object
	 */
	Object load(Object data);

	/**
	 * Checks whether the two objects are the same under this format.
	 * 
	 * @param a
	 *            the first object
	 * @param b
	 *            the second object
	 * @return whether the two objects are equal
	 */
	boolean equals(Object a, Object b);

	/**
	 * Parses the format marker
	 * 
	 * @param obj
	 *            the JSON marker
	 * @return the format
	 */
	public static IDataFormat parse(JSONObject obj) {
		String type = obj.getString("type");
		switch (type) {
		case "object":
			return new ObjectDataFormat(obj);
		case "primitive":
			return obj.getEnum(PrimitiveDataFormat.class, "value");
		default:
			throw new RuntimeException("Invalid obj type " + type);
		}
	}

}
