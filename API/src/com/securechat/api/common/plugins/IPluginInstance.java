package com.securechat.api.common.plugins;

import com.securechat.api.common.Sides;

/**
 * Represents a loaded plugin
 */
public interface IPluginInstance {

	/**
	 * Returns the name of the plugin.
	 * 
	 * @return the plugin name
	 */
	String getName();

	/**
	 * Returns the version of the plugin.
	 * 
	 * @return the plugin version
	 */
	String getVersion();

	/**
	 * Returns the sides that this plugin runs on.
	 * 
	 * @return the plugin side
	 */
	Sides getSide();

	/**
	 * Returns the instance of the main plugin class.
	 * 
	 * @return the plugin instance
	 */
	Object getInstance();

}
