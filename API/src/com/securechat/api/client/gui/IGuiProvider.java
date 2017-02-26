package com.securechat.api.client.gui;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Provides access to the GUI
 */
public interface IGuiProvider extends IImplementation {

	/**
	 * Initialises the display ready for a GUI to be opened. Calls ready once
	 * done.
	 * 
	 * @param ready
	 *            the method to call afterwards
	 */
	void init(Runnable ready);

	/**
	 * Gets the Keystore GUI instance.
	 * 
	 * @return keystore gui instance
	 */
	IKeystoreGui getKeystoreGui();

	/**
	 * Gets the Login GUI instance.
	 * 
	 * @return login gui instance
	 */
	IGui getLoginGui();

	/**
	 * Gets the Main GUI instance.
	 * 
	 * @return main gui instance
	 */
	IMainGui getMainGui();

	/**
	 * Displays a crash dialog and then exits once closed.
	 * 
	 * @param reason
	 *            the error
	 */
	void handleCrash(Throwable reason);

}
