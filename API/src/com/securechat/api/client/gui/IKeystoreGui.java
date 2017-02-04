package com.securechat.api.client.gui;

import com.securechat.api.common.security.IKeystore;

public interface IKeystoreGui extends IGui {

	public void init(IKeystore keystore);

}
