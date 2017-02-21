package com.securechat.api.common.database;

import org.json.JSONObject;

public interface IDataFormat {

	public boolean validate(Object value);

	public JSONObject toJSON();

	public Object save(Object data);

	public Object load(Object data);
	
	public boolean equals(Object a, Object b);

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
