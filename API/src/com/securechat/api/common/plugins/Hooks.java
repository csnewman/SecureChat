package com.securechat.api.common.plugins;

/**
 * All hooks that a plugin can tie into.
 */
public enum Hooks {
	/**
	 * Called just after the plugin manager and implementation factory have been
	 * loaded. Storage may not be fully available yet.
	 */
	EarlyInit,
	/**
	 * Called after the program has fully load and is ready to open the GUI.
	 * Intended for implementation registration.
	 */
	Init,
	/**
	 * Called directly after the init hook to allow any final configuration of
	 * components that require content from the init stage.
	 */
	LateInit
}
