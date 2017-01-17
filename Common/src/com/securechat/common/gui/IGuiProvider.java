package com.securechat.common.gui;

import com.securechat.common.implementation.IImplementation;
import com.securechat.common.security.IKeystore;

public interface IGuiProvider extends IImplementation{

	public void init();
	
	public void showKeystoreGui(IKeystore keystore);
	
}
