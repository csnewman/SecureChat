//package com.securechat.common;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//public class JsonObject {
//	private static ThreadLocal<JSONParser> localParser = new ThreadLocal<JSONParser>() {
//		protected JSONParser initialValue() {
//			return new JSONParser();
//		};
//	};
//	private JSONObject internal;
//
//	public JsonObject() {
//		internal = new JSONObject();
//	}
//
//	public JsonObject(JSONObject internal) {
//		this.internal = internal;
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> T get(String name, Class<T> clazz) {
//		if (clazz.isAssignableFrom(JsonObject.class)) {
//			return (T) new JsonObject((JSONObject) internal.get(name));
//		}
//		return (T) internal.get(name);
//	}
//
//	@SuppressWarnings("unchecked")
//	public void set(String name, Object value) {
//		if (value instanceof JsonObject) {
//			internal.put(name, ((JsonObject) value).getInternalObj());
//		} else {
//			internal.put(name, value);
//		}
//	}
//
//	public JSONObject getInternalObj() {
//		return internal;
//	}
//
//	public void writeFile(File file) {
//		try {
//			FileWriter writer = new FileWriter(file);
//			writer.write(toJsonString());
//			writer.close();
//		} catch (IOException e) {
//			throw new RuntimeException("Failed to save to file " + file.getAbsolutePath(), e);
//		}
//	}
//
//	public String toJsonString() {
//
//		return internal.toJSONString();
//	}
//
//	public static JsonObject parseFile(File file) {
//		JSONParser parser = localParser.get();
//		try {
//			JSONObject internal = (JSONObject) parser.parse(new FileReader(file));
//			return new JsonObject(internal);
//		} catch (IOException | ParseException e) {
//			throw new RuntimeException("Failed to load file " + file.getAbsolutePath(), e);
//		}
//	}
//
//}