package com.securechat.common.gui;

import com.securechat.common.implementation.IImplementation;
import com.securechat.common.security.IKeystore;

public interface IKeystoreGui extends IImplementation {

	public void show(IKeystore keystore);

}
