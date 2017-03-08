package com.securechat.api.client.gui;

import com.securechat.api.common.security.IKeystore;

/**
 * A keystore GUI used for unlocking and generating keystores.
 */
public interface IKeystoreGui extends IGui {

	/**
	 * Configures the GUI with the keystore to be unlocked.
	 * 
	 * @param keystore
	 *            keystore instance
	 */
	void init(IKeystore keystore);

}
