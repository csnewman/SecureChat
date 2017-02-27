package com.securechat.api.common.database;

import java.util.Arrays;
import java.util.Base64;

import org.json.JSONObject;

/**
 * Represents a primitive type
 */
public enum PrimitiveDataFormat implements IDataFormat {
	String, JSON, Integer, Long, Boolean, ByteArray;

	@Override
	public boolean validate(Object value) {
		switch (this) {
		case String:
			return value instanceof String;
		case JSON:
			return value instanceof JSONObject;
		case Integer:
			return value instanceof Integer;
		case Long:
			return value instanceof Long;
		case Boolean:
			return value instanceof Boolean;
		case ByteArray:
			return value instanceof byte[];
		default:
			return false;
		}
	}

	@Override
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", "primitive");
		obj.put("value", this);
		return obj;
	}

	@Override
	public boolean equals(Object a, Object b) {
		switch (this) {
		case String:
		case Integer:
		case JSON:
		case Long:
		case Boolean:
			return a.equals(b);
		case ByteArray:
			return Arrays.equals((byte[]) a, (byte[]) b);
		default:
			return false;
		}
	}

	@Override
	public Object save(Object data) {
		switch (this) {
		case String:
		case Integer:
		case Long:
		case JSON:
		case Boolean:
			return data;
		case ByteArray:
			// Stores byte array as a Base64 encoded string
			return Base64.getEncoder().encodeToString((byte[]) data);
		default:
			return null;
		}
	}

	@Override
	public Object load(Object data) {
		switch (this) {
		case String:
		case Integer:
		case Long:
		case JSON:
		case Boolean:
			return data;
		case ByteArray:
			// Loads a Base64 encoded string as a byte array
			return Base64.getDecoder().decode((String) data);
		default:
			return null;
		}
	}

}
