package com.securechat.plugins.jsondatabase;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.securechat.api.common.database.IDatabase;
import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataFormat;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.storage.IStorage;

public class JsonDatabase implements IDatabase {
	public static final ImplementationMarker MARKER = new ImplementationMarker(JsonDatabasePlugin.NAME,
			JsonDatabasePlugin.VERSION, "json_database", "1.0.0");
	@InjectInstance
	private IImplementationFactory factory;
	@InjectInstance
	private IStorage storage;
	private Map<String, JsonTable> tables;

	@Override
	public void init() {
		tables = new HashMap<String, JsonTable>();

		if (!storage.doesFileExist("database/tables.json"))
			return;

		JSONObject obj = storage.readJsonFile("database/tables.json");
		JSONArray array = obj.getJSONArray("tables");

		for (int i = 0; i < array.length(); i++) {
			String name = array.getString(i);
			JsonTable table = new JsonTable(name);
			factory.inject(table);
			table.load();
			tables.put(name, table);
		}

		save();
	}

	private void save() {
		JSONObject tablesFile = new JSONObject();
		JSONArray tablesArray = new JSONArray();
		for (String name : tables.keySet()) {
			tablesArray.put(name);
		}
		tablesFile.put("tables", tablesArray);
		storage.writeJsonFile("database/tables.json", tablesFile);
	}

	@Override
	public void ensureTable(String name, ObjectDataFormat format) {
		if (!tableExists(name)) {
			createTable(name, format);
		} else {
			if (!getTable(name).getFormat().equals(format)) {
				throw new RuntimeException("Invalid format on file for "+name+"!");
			}
		}
	}

	@Override
	public void createTable(String name, ObjectDataFormat format) {
		JsonTable table = new JsonTable(name, format);
		factory.inject(table);
		table.save();
		tables.put(name, table);
		save();
	}

	@Override
	public ITable getTable(String name) {
		return tables.get(name);
	}

	@Override
	public boolean tableExists(String name) {
		return tables.containsKey(name);
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}