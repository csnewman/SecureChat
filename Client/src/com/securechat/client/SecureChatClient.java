package com.securechat.client;

import java.io.File;

import com.securechat.common.FallbackLogger;
import com.securechat.common.IContext;
import com.securechat.common.ILogger;
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
	private ILogger logger;

	public void init() {
		settings = new PropertyCollection(null);
		if (settingsFile.exists())
			settings.loadFile(settingsFile);
		saveSettings();

		logger = new FallbackLogger();
		logger.init(this);

		implementationFactory = new ImplementationFactory(logger, settings.getPermissive(defaultsProp));
		implementationFactory.registerFixedInstance("context", IContext.class, this);
		implementationFactory.registerFixed("logger_context", ILogger.class, this::getLogger);

		pluginManager = new PluginManager(this);
		pluginManager.loadPlugins();
		pluginManager.regeneateCache();

		pluginManager.invokeHook(Hooks.EarlyInit, this);

		logger = implementationFactory.provide(ILogger.class);
		
		logger.init(this);
		logger.debug("Logger provider: " + logger);

		pluginManager.invokeHook(Hooks.Init, this);

		IGuiProvider provider = implementationFactory.provide(IGuiProvider.class);
		logger.debug("Gui provider: " + provider);

		IKeystore keystore = implementationFactory.provide(IKeystore.class);
		logger.info("keystore " + keystore);
		keystore.generate("".toCharArray());

		provider.init();
		// provider.newKeystoreGui().show();

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
	public ILogger getLogger() {
		return logger;
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

	@Override
	public String getImplName() {
		return getAppName();
	}

	public static void main(String[] args) {
		INSTANCE = new SecureChatClient();
		INSTANCE.init();
	}

}
