package com.securechat.plugins.sqldatabase;

import java.sql.SQLException;

import org.json.JSONObject;

import com.securechat.api.common.IContext;
import com.securechat.api.common.database.IDataFormat;
import com.securechat.api.common.database.IDatabase;
import com.securechat.api.common.database.ITable;
import com.securechat.api.common.database.ObjectDataFormat;
import com.securechat.api.common.database.PrimitiveDataFormat;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PrimitiveProperty;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.common.storage.IStorage;

/**
 * A reference implementation of the SQL database.
 */
public class SqlDatabase implements IDatabase {
	@InjectInstance
	private IContext context;
	@InjectInstance
	private IImplementationFactory factory;
	@InjectInstance
	private IStorage storage;
	private String connectionUrl;

	@Override
	public void init() {
		// Loads the connection url
		PropertyCollection collection = context.getSettings().getPermissive(SQL_PROPERTY);
		connectionUrl = collection.getPermissive(CONNECTION_URL_PROPERY);
	}

	public DatabaseConnection createConnection() {
		return new DatabaseConnection(connectionUrl);
	}

	@Override
	public void ensureTable(String name, ObjectDataFormat format) {
		if (!tableExists(name)) {
			createTable(name, format);
		} else {
			if (!getTable(name).getFormat().equals(format)) {
				throw new RuntimeException("Invalid format on file for " + name + "!");
			}
		}
	}

	@Override
	public void createTable(String name, ObjectDataFormat format) {
		// Stores the format
		PropertyCollection collection = context.getSettings().getPermissive(SQL_PROPERTY);
		collection.set(new PrimitiveProperty<String>(name), format.toJSON().toString());
		context.saveSettings();

		// Creates the SQL operation
		String operation = "CREATE TABLE " + name + " (";

		// Add primary key
		String primary = format.getPrimary();
		operation += primary + " " + getSqlType(format.getFormat(primary)) + " NOT NULL PRIMARY KEY UNIQUE";

		// Add other fields
		for (String field : format.getNames()) {
			if (field.equals(primary))
				continue;
			operation += ", " + field + " " + getSqlType(format.getFormat(field)) + " NOT NULL";
		}
		operation += ")";

		DatabaseConnection c = createConnection();
		try {
			// Executes statement
			c.getStatement(operation).execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			c.close();
		}
	}

	public String getSqlType(IDataFormat format) {
		if (format instanceof PrimitiveDataFormat) {
			switch ((PrimitiveDataFormat) format) {
			case Integer:
			case Long:
			default:
				return "TEXT";
			}
		}
		return "TEXT";
	}

	public String sqlSerialise(IDataFormat format, Object value) {
		return format.save(value).toString();
	}

	public Object sqlDeserialise(IDataFormat format, Object value) {
		if (format instanceof PrimitiveDataFormat) {
			switch ((PrimitiveDataFormat) format) {
			case Integer:
				return Integer.parseInt(value.toString());
			case Long:
				return Long.parseLong(value.toString());
			case Boolean:
				return Boolean.parseBoolean(value.toString());
			default:
				return format.load(value);
			}
		}
		return format.load(value);
	}

	@Override
	public ITable getTable(String name) {
		// Loads the format
		PropertyCollection collection = context.getSettings().getPermissive(SQL_PROPERTY);
		ObjectDataFormat format = new ObjectDataFormat(
				new JSONObject(collection.get(new PrimitiveProperty<String>(name))));
		// Creates a table instance
		return new SqlTable(this, name, format);
	}

	@Override
	public boolean tableExists(String name) {
		DatabaseConnection connection = createConnection();
		try {
			return connection.getRawConnection().getMetaData().getTables(null, null, name, null).next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	private static final PrimitiveProperty<String> CONNECTION_URL_PROPERY;
	private static final CollectionProperty SQL_PROPERTY;
	static {
		MARKER = new ImplementationMarker(SqlDatabasePlugin.NAME, SqlDatabasePlugin.VERSION, "sql_database", "1.0.0");
		CONNECTION_URL_PROPERY = new PrimitiveProperty<String>("connection-url", "^[a-zA-Z]\\w*$");
		SQL_PROPERTY = new CollectionProperty("sql-database", CONNECTION_URL_PROPERY);
	}

}
