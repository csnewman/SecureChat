package com.securechat.api.client.gui;

import com.securechat.api.common.implementation.IImplementation;

/**
 * A basic GUI.
 */
public interface IGui extends IImplementation {

	/**
	 * Opens the GUI.
	 */
	void open();

	/**
	 * Closes the GUI.
	 */
	void close();

	/**
	 * Checks whether the GUI is currently open.
	 * 
	 * @return whether the GUI is open
	 */
	boolean isOpen();

	/**
	 * Waits until the GUI has closed.
	 */
	void awaitClose();

}
