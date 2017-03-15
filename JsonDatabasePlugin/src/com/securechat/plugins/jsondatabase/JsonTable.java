package com.securechat.plugins.jsondatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataFormat;
import com.securechat.api.common.database.ObjectDataInstance;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.storage.IStorage;

/**
 * A reference implementation of a JSON table.
 */
public class JsonTable implements ITable {
	private String name;
	@InjectInstance
	private IStorage storage;
	private ObjectDataFormat format;
	private ObjectDataInstance[] rows;
	private Map<Object, Integer> primaryCache;

	public JsonTable(String name) {
		this.name = name;
		primaryCache = new HashMap<Object, Integer>();
	}

	public JsonTable(String name, ObjectDataFormat format) {
		this.name = name;
		this.format = format;
		primaryCache = new HashMap<Object, Integer>();
		rows = new ObjectDataInstance[0];
	}

	public void load() {
		JSONObject obj = storage.readJsonFile("database/" + name + ".json");
		format = new ObjectDataFormat(obj.getJSONObject("format"));

		// Loads the rows
		JSONArray array = obj.getJSONArray("rows");
		rows = new ObjectDataInstance[array.length()];
		for (int i = 0; i < array.length(); i++) {
			rows[i] = new ObjectDataInstance(format, array.getJSONObject(i));
			primaryCache.put(rows[i].getPrimary(), i);
		}

		save();
	}

	public void save() {
		JSONObject obj = new JSONObject();
		obj.put("format", format.toJSON());

		// Saves the rows
		JSONArray array = new JSONArray();
		for (ObjectDataInstance row : rows) {
			array.put(row.toJSON());
		}
		obj.put("rows", array);

		storage.writeJsonFile("database/" + name + ".json", obj);
	}

	@Override
	public void insertRow(ObjectDataInstance data) {
		if (!format.validate(data)) {
			throw new RuntimeException("Invalid row data");
		}

		// Checks the primary key is unique
		Object primary = data.getPrimary();
		if (primaryCache.containsKey(primary)) {
			throw new RuntimeException("Primary key already in use!");
		}

		// Inserts the row
		ObjectDataInstance[] temp = new ObjectDataInstance[rows.length + 1];
		System.arraycopy(rows, 0, temp, 0, rows.length);
		temp[temp.length - 1] = data;
		primaryCache.put(primary, temp.length - 1);
		rows = temp;

		save();
	}

	@Override
	public ObjectDataInstance[] getAllRows() {
		return rows;
	}

	@Override
	public ObjectDataInstance getRow(Object primaryValue) {
		Integer id = primaryCache.get(primaryValue);
		return id != null ? rows[id] : null;
	}

	@Override
	public void updateRow(Object primaryKey, ObjectDataInstance data) {
		ObjectDataInstance row = getRow(primaryKey);
		for (Entry<String, Object> entry : data.getValues().entrySet()) {
			row.setField(entry.getKey(), entry.getValue());
		}
		save();
	}

	@Override
	public ObjectDataInstance[] getRows(ObjectDataInstance data) {
		List<ObjectDataInstance> found = new ArrayList<ObjectDataInstance>();
		for (ObjectDataInstance row : rows) {
			if (row.matches(data)) {
				found.add(row);
			}
		}
		return found.toArray(new ObjectDataInstance[0]);
	}

	@Override
	public void deleteRow(Object primaryValue) {
		Integer id = primaryCache.get(primaryValue);

		if (id == null) {
			return;
		}

		ObjectDataInstance[] temp = new ObjectDataInstance[rows.length - 1];
		if (id > 0)
			System.arraycopy(rows, 0, temp, 0, id);
		if (id + 1 < rows.length)
			System.arraycopy(rows, id + 1, temp, id, rows.length - id - 1);
		rows = temp;

		primaryCache.clear();
		String primarykey = format.getPrimary();
		for (int i = 0; i < rows.length; i++) {
			primaryCache.put(rows[i].getField(primarykey), i);
		}

		save();
	}

	@Override
	public ObjectDataFormat getFormat() {
		return format;
	}

}
