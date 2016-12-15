package com.securechat.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getOrDefault(JSONObject obj, String name, T defaultValue, Class<T> type) {
		return obj.has(name) ? (T) obj.get(name) : defaultValue;
	}

	public static JSONObject parseFile(File file) {
		try {
			return new JSONObject(Util.readFileToString(file));
		} catch (JSONException | IOException e) {
			throw new RuntimeException("Failed to parse file " + file.getAbsolutePath(), e);
		}
	}

	public static void writeFile(File file, JSONObject obj) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(obj.toString(4));
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to write file", e);
		}
	}

}
