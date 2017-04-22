package com.securechat.api.common.plugins;

/**
 * Manages and stores all plugins and handles the invocation of hooks.
 */
public interface IPluginManager {

	/**
	 * Searches all classes for the plugin annotation and attempts to load them.
	 */
	void loadPlugins();

	/**
	 * Regenerates the hook invocation order cache.
	 */
	void regeneateCache();

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
