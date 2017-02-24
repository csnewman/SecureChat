package com.securechat.api.client.gui;

import com.securechat.api.common.implementation.IImplementation;

public interface IGuiProvider extends IImplementation {

	public void init(Runnable ready);

	public IKeystoreGui getKeystoreGui();

	public IGui getLoginGui();

	public IGui getMainGui();

}
