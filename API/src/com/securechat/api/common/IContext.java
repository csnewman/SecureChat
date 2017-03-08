package com.securechat.api.common;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.IPluginManager;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.common.storage.IStorage;

/**
 * The context that the program is running under. Provides access to core
 * features of the host program.
 */
public interface IContext extends IImplementation {

	/**
	 * Gets the plugin manager instance.
	 * 
	 * @return the plugin manager instance
	 */
	IPluginManager getPluginManager();

	/**
	 * Gets the implementation factory instance
	 * 
	 * @return the implementation factory instance
	 */
	IImplementationFactory getImplementationFactory();

	/**
	 * Gets the settings file
	 * 
	 * @return the settings
	 */
	PropertyCollection getSettings();

	/**
	 * Saves any changes made to the settings
	 */
	void saveSettings();

	/**
	 * Handles a crash that has occurred outside of the auto detection range.
	 * 
	 * @param reason
	 *            the error
	 */
	void handleCrash(Throwable reason);

	/**
	 * Causes the program to exit
	 */
	void exit();

	/**
	 * Gets the logger instance
	 * 
	 * @return the logger
	 */
	ILogger getLogger();

	/**
	 * Gets the storage instance
	 * 
	 * @return the storage instance
	 */
	IStorage getStorage();

	/**
	 * Gets the side the context is responsible for.
	 * 
	 * @return the current side.
	 */
	Sides getSide();

	/**
	 * Gets the name of the context implementation.
	 * 
	 * @return the name
	 */
	String getAppName();

	/**
	 * Gets the version of the context implementation.
	 * 
	 * @return the version
	 */
	String getAppVersion();

	/**
	 * Gets the OS type we are running on.
	 * 
	 * @return the OS type
	 */
	OsType getOsType();

	/**
	 * Gets the architecture of the platform we are running on.
	 * 
	 * @return the architecture
	 */
	PlatformArch getPlatformArch();

}