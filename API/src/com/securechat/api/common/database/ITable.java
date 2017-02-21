package com.securechat.api.common.database;

public interface ITable {

	public void insertRow(ObjectDataInstance data);

	public ObjectDataInstance[] getAllRows();

	public ObjectDataInstance getRow(Object primaryKey);

	public ObjectDataInstance[] getRows(ObjectDataInstance data);
	
	public void deleteRow(Object primaryKey);
	
	public ObjectDataFormat getFormat();
}
