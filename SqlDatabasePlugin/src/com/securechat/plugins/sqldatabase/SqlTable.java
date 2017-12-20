package com.securechat.plugins.sqldatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataFormat;
import com.securechat.api.common.database.ObjectDataInstance;

/**
 * A reference implementation of a JSON table.
 */
public class SqlTable implements ITable {
	private SqlDatabase database;
	private String name;
	private ObjectDataFormat dataFormat;

	public SqlTable(SqlDatabase database, String name, ObjectDataFormat dataFormat) {
		this.database = database;
		this.name = name;
		this.dataFormat = dataFormat;
	}

	@Override
	public void insertRow(ObjectDataInstance data) {
		Map<String, Object> values = data.getValues();

		// Create SQL statement
		String operation = "INSERT INTO " + name + " (";

		// Add column names
		boolean added = false;
		for (String key : values.keySet()) {
			if (added)
				operation += ", ";
			added = true;
			operation += key;
		}

		// Add value markers
		operation += ") VALUES (";
		for (int i = 0; i < values.size(); i++) {
			if (i != 0)
				operation += ", ";
			operation += "?";
		}
		operation += ")";

		DatabaseConnection connection = database.createConnection();
		try {
			PreparedStatement statement = connection.getStatement(operation);

			// Store values into markers
			int index = 1;
			for (Entry<String, Object> entry : values.entrySet()) {
				statement.setObject(index,
						database.sqlSerialise(dataFormat.getFormat(entry.getKey()), entry.getValue()));
				index++;
			}
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
	}

	@Override
	public ObjectDataInstance[] getAllRows() {
		DatabaseConnection connection = database.createConnection();
		try {
			PreparedStatement statement = connection.getStatement("SELECT * FROM " + name);
			ResultSet set = statement.executeQuery();
			ArrayList<ObjectDataInstance> results = new ArrayList<ObjectDataInstance>();
			while (set.next())
				results.add(convert(set));
			return results.toArray(new ObjectDataInstance[0]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
	}

	@Override
	public ObjectDataInstance getRow(Object primaryValue) {
		DatabaseConnection connection = database.createConnection();
		try {
			PreparedStatement statement = connection
					.getStatement("SELECT * FROM " + name + " WHERE " + dataFormat.getPrimary() + "=?");
			statement.setObject(1, database.sqlSerialise(dataFormat.getFormat(dataFormat.getPrimary()), primaryValue));
			ResultSet set = statement.executeQuery();
			if (set.next())
				return convert(set);
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
	}

	private ObjectDataInstance convert(ResultSet set) {
		try {
			ObjectDataInstance instance = new ObjectDataInstance(dataFormat);
			for (String field : dataFormat.getNames()) {
				instance.setField(field, database.sqlDeserialise(dataFormat.getFormat(field), set.getObject(field)));
			}
			return instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateRow(Object primaryKey, ObjectDataInstance data) {
		Map<String, Object> values = data.getValues();

		// Create SQL statement
		String operation = "UPDATE " + name + " SET ";
		// Add column names
		boolean added = false;
		for (String key : values.keySet()) {
			if (added)
				operation += ", ";
			added = true;
			operation += key + "=?";
		}
		// Add primary key selection
		operation += " WHERE " + dataFormat.getPrimary() + "=?";

		DatabaseConnection connection = database.createConnection();
		try {
			PreparedStatement statement = connection.getStatement(operation);

			// Store values into markers
			int index = 1;
			for (Entry<String, Object> entry : values.entrySet()) {
				statement.setObject(index,
						database.sqlSerialise(dataFormat.getFormat(entry.getKey()), entry.getValue()));
				index++;
			}
			statement.setObject(index,
					database.sqlSerialise(dataFormat.getFormat(dataFormat.getPrimary()), primaryKey));
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
	}

	@Override
	public ObjectDataInstance[] getRows(ObjectDataInstance data) {
		Map<String, Object> values = data.getValues();

		// Create SQL statement
		String operation = "SELECT * FROM " + name + " WHERE ";
		// Add column names
		boolean added = false;
		for (String key : values.keySet()) {
			if (added)
				operation += " AND ";
			added = true;
			operation += key + "=?";
		}

		DatabaseConnection connection = database.createConnection();
		try {
			PreparedStatement statement = connection.getStatement(operation);

			// Store values into markers
			int index = 1;
			for (Entry<String, Object> entry : values.entrySet()) {
				statement.setObject(index,
						database.sqlSerialise(dataFormat.getFormat(entry.getKey()), entry.getValue()));
				index++;
			}
			ResultSet set = statement.executeQuery();
			ArrayList<ObjectDataInstance> results = new ArrayList<ObjectDataInstance>();
			while (set.next())
				results.add(convert(set));
			return results.toArray(new ObjectDataInstance[0]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
	}

	@Override
	public void deleteRow(Object primaryValue) {
		DatabaseConnection connection = database.createConnection();
		try {
			PreparedStatement statement = connection
					.getStatement("DELETE FROM " + name + " WHERE " + dataFormat.getPrimary() + "=?");
			statement.setObject(1, database.sqlSerialise(dataFormat.getFormat(dataFormat.getPrimary()), primaryValue));
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
	}

	@Override
	public ObjectDataFormat getFormat() {
		return dataFormat;
	}

}
