package com.securechat.api.common.database;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Provides access to a database connection.
 */
public interface IDatabase extends IImplementation {

	/**
	 * Configures the database.
	 */
	void init();

	/**
	 * Creates a table in the database.
	 * 
	 * @param name
	 *            the name of the table
	 * @param format
	 *            the format of the table
	 */
	void createTable(String name, ObjectDataFormat format);

	/**
	 * Ensures a table exists with the given format, if not creates one.
	 * 
	 * @param name
	 *            the name of the table
	 * @param format
	 *            the format of the table
	 */
	void ensureTable(String name, ObjectDataFormat format);

	/**
	 * Checks whether a table exists.
	 * 
	 * @param name
	 *            the name of the table
	 * @return whether the table exists
	 */
	boolean tableExists(String name);

	/**
	 * Gets a table by its given name.
	 * 
	 * @param name
	 *            the name of the table
	 * @return the table
	 */
	ITable getTable(String name);

}
