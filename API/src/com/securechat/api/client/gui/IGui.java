package com.securechat.api.client.gui;

import com.securechat.api.common.implementation.IImplementation;

public interface IGui extends IImplementation{
	
	public void open();
	
	public void close();
	
	public boolean isOpen();
	
	public void awaitClose();
	
}
