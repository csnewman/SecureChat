package com.securechat.api.client.gui;

import com.securechat.api.common.implementation.IImplementation;

public interface ILoginGui extends IImplementation{
	
	public void open();
	
	public void close();
	
	public boolean isOpen();
	
}
