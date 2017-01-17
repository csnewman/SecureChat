package com.securechat.common;

import com.securechat.common.implementation.IImplementation;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.PluginManager;
import com.securechat.common.plugins.Sides;
import com.securechat.common.properties.PropertyCollection;

public interface IContext extends IImplementation {

	public PluginManager getPluginManager();

	public ImplementationFactory getImplementationFactory();

	public PropertyCollection getSettings();

	public void saveSettings();

	public void exit();

	public ILogger getLogger();

	public IStorage getStorage();

	public Sides getSide();

	public String getAppName();

	public String getAppVersion();

}
