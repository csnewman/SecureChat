package com.securechat.api.common.database;

import com.securechat.api.common.implementation.IImplementation;

public interface IDatabase extends IImplementation{
	
	public void init();
	
	public void createTable(String name, ObjectDataFormat format);

	public void ensureTable(String name, ObjectDataFormat format);
	
	public boolean tableExists(String name);
	
	public ITable getTable(String name);
	
}
