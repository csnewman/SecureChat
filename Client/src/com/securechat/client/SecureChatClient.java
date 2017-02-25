package com.securechat.client;

import com.securechat.api.client.IClientManager;
import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.client.gui.IKeystoreGui;
import com.securechat.api.client.network.IClientNetworkManager;
import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
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
	public static final ImplementationMarker MARKER = new ImplementationMarker("official", "1.0.0", "client", "1.0.0");
	public static final CollectionProperty IMPLEMENTATIONS_PROPERTY = new CollectionProperty("implementations");
	private static final String settingsFile = "settings.json";
	private static SecureChatClient INSTANCE;
	private PropertyCollection settings;
	private PluginManager pluginManager;
	private ImplementationFactory implementationFactory;
	private ILogger logger;
	private IStorage storage;
	private IGuiProvider gui;

	public void init(IStorage storage) {
		this.storage = storage;
		storage.init();

		logger = new FallbackLogger();
		logger.init(this);

		logger.info("SecureChatClient (" + MARKER.getId() + ")");

		settings = new PropertyCollection(null);
		if (storage.doesFileExist(settingsFile))
			settings.loadFile(storage, settingsFile);
		saveSettings();

		implementationFactory = new ImplementationFactory(logger, settings.getPermissive(IMPLEMENTATIONS_PROPERTY));
		implementationFactory.set(IContext.class, this);
		implementationFactory.set(IStorage.class, storage);
		implementationFactory.set(IImplementationFactory.class, implementationFactory);
		implementationFactory.register(FallbackLogger.MARKER, ILogger.class, FallbackLogger::new);
		implementationFactory.register(ByteReader.MARKER, IByteReader.class, ByteReader::new);
		implementationFactory.register(ByteWriter.MARKER, IByteWriter.class, ByteWriter::new);
		implementationFactory.register(ClientManager.MARKER, IClientManager.class, ClientManager::new);
		implementationFactory.setFallbackDefault(ILogger.class, FallbackLogger.MARKER);
		implementationFactory.setFallbackDefault(IByteReader.class, ByteReader.MARKER);
		implementationFactory.setFallbackDefault(IByteWriter.class, ByteWriter.MARKER);
		implementationFactory.setFallbackDefault(IClientManager.class, ClientManager.MARKER);
		implementationFactory.inject(storage);

		pluginManager = new PluginManager(this);
		pluginManager.loadPlugins();
		pluginManager.regeneateCache();

		pluginManager.invokeHook(Hooks.EarlyInit, this);
		implementationFactory.inject(storage);

		logger = implementationFactory.provide(ILogger.class);
		implementationFactory.set(ILogger.class, logger);
		logger.init(this);
		logger.debug("Logger provider: " + logger);

		pluginManager.invokeHook(Hooks.Init, this);
		pluginManager.invokeHook(Hooks.LateInit, this);

		gui = implementationFactory.get(IGuiProvider.class, true);
		logger.debug("Gui provider: " + gui);

		saveSettings();

		IClientManager clientManager = implementationFactory.get(IClientManager.class, true);
		clientManager.init();

		gui.init(this::guiReady);
	}

	private void guiReady() {
		IKeystore keystore = implementationFactory.get(IKeystore.class, true);
		logger.info("Keystore: " + keystore);

		IKeystoreGui kgui = gui.getKeystoreGui();
		kgui.init(keystore);
		kgui.open();
		kgui.awaitClose();

		IConnectionStore store = implementationFactory.get(IConnectionStore.class, true);
		logger.info("Connection Store: " + store);
		store.init();

		IClientNetworkManager networkManager = implementationFactory.get(IClientNetworkManager.class, true);
		logger.info("Network Manager: " + networkManager);

		gui.getLoginGui().open();
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
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[2];
		logger.info("Exit requested by " + e.getMethodName() + " in " + e.getClassName());
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
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static void main(String[] args) {
		INSTANCE = new SecureChatClient();
		INSTANCE.init(new FileStorage());
	}

}
