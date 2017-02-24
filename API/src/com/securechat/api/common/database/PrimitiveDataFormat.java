package com.securechat.api.common.database;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

public enum PrimitiveDataFormat implements IDataFormat {
	String, JSON, Integer, Long, ByteArray;

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
			return data;
		case ByteArray:
			byte[] array = (byte[]) data;
			JSONArray out = new JSONArray();
			for (byte b : array) {
				out.put(b);
			}
			return out;
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
			return data;
		case ByteArray:
			JSONArray array = (JSONArray) data;
			byte[] out = new byte[array.length()];
			for (int i = 0; i < array.length(); i++) {
				out[i] = (byte) array.getInt(i);
			}
			return out;
		default:
			return null;
		}
	}

}
