package com.securechat.client;

import java.io.File;

import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;
import com.securechat.common.FallbackLogger;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.PluginManager;
import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;
import com.securechat.common.storage.FileStorage;

public class SecureChatClient implements IContext {
	public static final CollectionProperty defaultsProp = new CollectionProperty("defaults");
	private static final String settingsFile = "settings.json";
	private static SecureChatClient INSTANCE;
	private PropertyCollection settings;
	private PluginManager pluginManager;
	private ImplementationFactory implementationFactory;
	private ILogger logger;
	private IStorage storage;

	public void init(IStorage storage) {
		this.storage = storage;
		storage.init();

		logger = new FallbackLogger();
		logger.init(this);

		settings = new PropertyCollection(null);
		if (storage.doesFileExist(settingsFile))
			settings.loadFile(storage, settingsFile);
		saveSettings();

		implementationFactory = new ImplementationFactory(logger, settings.getPermissive(defaultsProp));
		implementationFactory.set(IContext.class, this);
		implementationFactory.set(IStorage.class, storage);
		implementationFactory.set(IImplementationFactory.class, implementationFactory);
		implementationFactory.register("fallback", ILogger.class, FallbackLogger::new);
		implementationFactory.register("official-byte_reader", IByteReader.class, ByteReader::new);
		implementationFactory.register("official-byte_writer", IByteWriter.class, ByteWriter::new);
		implementationFactory.setFallbackDefault(ILogger.class, "fallback");
		implementationFactory.setFallbackDefault(IByteReader.class, "official-byte_reader");
		implementationFactory.setFallbackDefault(IByteWriter.class, "official-byte_writer");

		pluginManager = new PluginManager(this);
		pluginManager.loadPlugins();
		pluginManager.regeneateCache();

		pluginManager.invokeHook(Hooks.EarlyInit, this);

		logger = implementationFactory.provide(ILogger.class);
		implementationFactory.set(ILogger.class, logger);
		logger.init(this);
		logger.debug("Logger provider: " + logger);

		pluginManager.invokeHook(Hooks.Init, this);
		pluginManager.invokeHook(Hooks.LateInit, this);

		saveSettings();

		IGuiProvider gui = implementationFactory.get(IGuiProvider.class, true);
		logger.debug("Gui provider: " + gui);
		gui.init();

		IKeystore keystore = implementationFactory.get(IKeystore.class, true);
		logger.info("Keystore: " + keystore);

		gui.showKeystoreGui(keystore);
		gui.getLoginGui().open();

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
		settings.saveToFile(storage, settingsFile);
	}

	@Override
	public void exit() {
		saveSettings();
		System.exit(0);
	}

	@Override
	public ILogger getLogger() {
		return logger;
	}

	@Override
	public IStorage getStorage() {
		return storage;
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
		INSTANCE.init(new FileStorage());
	}

}
