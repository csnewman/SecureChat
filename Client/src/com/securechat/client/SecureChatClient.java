package com.securechat.client;

import java.io.File;

import com.securechat.common.IContext;
import com.securechat.common.gui.IGuiProvider;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.Hooks;
import com.securechat.common.plugins.PluginManager;
import com.securechat.common.plugins.Sides;
import com.securechat.common.properties.CollectionProperty;
import com.securechat.common.properties.PropertyCollection;
import com.securechat.common.security.IKeystore;

public class SecureChatClient implements IContext {
	public static final CollectionProperty defaultsProp = new CollectionProperty("defaults");
	private static final File settingsFile = new File("settings.json");
	private static SecureChatClient INSTANCE;
	private PropertyCollection settings;
	private PluginManager pluginManager;
	private ImplementationFactory implementationFactory;

	public void init() {
		settings = new PropertyCollection(null);
		if (settingsFile.exists())
			settings.loadFile(settingsFile);
		saveSettings();

		pluginManager = new PluginManager(Sides.Client);
		pluginManager.loadPlugins();
		pluginManager.regeneateCache();

		implementationFactory = new ImplementationFactory(settings.getPermissive(defaultsProp));

		pluginManager.invokeHook(Hooks.Init, this);
		
		implementationFactory.setConfigDefault(IGuiProvider.class, "official-basic_gui");
		
		IGuiProvider provider = implementationFactory.provide(IGuiProvider.class);
		System.out.println("Gui provider: "+provider);
		
		provider.init();
		
		provider.newKeystoreGui().show(new IKeystore() {
			
			@Override
			public String getImplName() {
				return null;
			}
			
			@Override
			public boolean load(char[] password) {
				return false;
			}
			
			@Override
			public boolean isLoaded() {
				return false;
			}
			
			@Override
			public boolean generate(char[] password) {
				return false;
			}
			
			@Override
			public boolean exists() {
				return false;
			}
		});
		
		saveSettings();
	}

	@Override
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	@Override
	public ImplementationFactory getImplementationFactory() {
		return implementationFactory;
	}

	@Override
	public PropertyCollection getSettings() {
		return settings;
	}

	@Override
	public void saveSettings() {
		settings.saveToFile(settingsFile);
	}

	@Override
	public Sides getSide() {
		return Sides.Client;
	}

	@Override
	public String getAppName() {
		return "official-client";
	}

	@Override
	public String getAppVersion() {
		return "1.0.0";
	}

	public static void main(String[] args) {
		INSTANCE = new SecureChatClient();
		INSTANCE.init();
	}

}
