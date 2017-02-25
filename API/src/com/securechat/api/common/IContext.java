package com.securechat.api.common;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.IPluginManager;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.common.storage.IStorage;

public interface IContext extends IImplementation {

	public IPluginManager getPluginManager();

	public IImplementationFactory getImplementationFactory();

	public PropertyCollection getSettings();

	public void saveSettings();

	public void handleCrash(Throwable reason);
	
	public void exit();

	public ILogger getLogger();

	public IStorage getStorage();

	public Sides getSide();

	public String getAppName();

	public String getAppVersion();

}