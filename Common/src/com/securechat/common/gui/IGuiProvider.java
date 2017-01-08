package com.securechat.common.gui;

import com.securechat.common.implementation.IImplementation;

public interface IGuiProvider extends IImplementation{

	public void init();
	
	public IKeystoreGui newKeystoreGui();
	
}
