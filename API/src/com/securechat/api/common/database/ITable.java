package com.securechat.api.common.database;

/**
 * A collection of rows, all of the same format.
 */
public interface ITable {

	/**
	 * Inserts a row into the table.
	 * 
	 * @param data
	 *            the row
	 */
	void insertRow(ObjectDataInstance data);

	/**
	 * Gets all rows stored in the table.
	 * 
	 * @return all the rows
	 */
	ObjectDataInstance[] getAllRows();

	/**
	 * Gets the row associated with the primary key value provided.
	 * 
	 * @param primaryKey
	 *            the primary key value
	 * @return the row
	 */
	ObjectDataInstance getRow(Object primaryKey);

	/**
	 * Gets all rows that fulfil the search.
	 * 
	 * @param data
	 *            the search data
	 * @return all rows that fulfil the search
	 */
	ObjectDataInstance[] getRows(ObjectDataInstance data);

	/**
	 * Updates the row associated with the primary key value with the given
	 * data.
	 * 
	 * @param primaryKey
	 *            the primary key value
	 * @param data
	 *            the data to overwrite
	 */
	void updateRow(Object primaryKey, ObjectDataInstance data);

	/**
	 * Deletes the row associated with the primary key value.
	 * 
	 * @param primaryKey
	 *            the primary key value
	 */
	void deleteRow(Object primaryKey);

	/**
	 * Returns the format of this table.
	 * 
	 * @return the table format
	 */
	ObjectDataFormat getFormat();
}
