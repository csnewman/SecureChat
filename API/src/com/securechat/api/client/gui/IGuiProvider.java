package com.securechat.api.client.gui;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.security.IKeystore;

public interface IGuiProvider extends IImplementation{

	public void init();
	
	public void showKeystoreGui(IKeystore keystore);
	
	public ILoginGui getLoginGui();
	
}
