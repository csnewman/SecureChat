package com.securechat.api.common.plugins;

public interface IPluginManager {

	public void invokeHook(Hooks hook, Object... objects);

	public void loadPlugins();

	public void regeneateCache();

}
