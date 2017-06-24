package com.securechat.api.common.plugins;

import java.util.List;

/**
 * Manages and stores all plugins and handles the invocation of hooks.
 */
public interface IPluginManager {

	/**
	 * Searches all classes for the plugin annotation and attempts to load them.
	 */
	void loadPlugins();

	/**
	 * Returns all loaded plugins
	 * 
	 * @return all loaded plugins
	 */
	List<IPluginInstance> getPlugins();

	/**
	 * Regenerates the hook invocation order cache.
	 */
	void regenerateCache();

	/**
	 * Calls the hook with the given arguments.
	 * 
	 * @param hook
	 *            the type of hook
	 * @param params
	 *            the arguments
	 */
	void invokeHook(Hooks hook, Object... params);

}
