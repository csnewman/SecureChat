package com.securechat.client;

import com.securechat.api.client.IClientManager;
import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.client.gui.IKeystoreGui;
import com.securechat.api.client.network.IClientNetworkManager;
import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.OsType;
import com.securechat.api.common.PlatformArch;
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
import com.securechat.common.ConsoleLogger;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.PluginManager;
import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;
import com.securechat.common.storage.FileStorage;

/**
 * The client context.
 */
public class SecureChatClient implements IContext {
	private PropertyCollection settings;
	private PluginManager pluginManager;
	private ImplementationFactory implementationFactory;
	private ILogger logger;
	private IStorage storage;
	private IGuiProvider gui;
	private boolean showDebug;

	public void init(IStorage storage, boolean showDebug) {
		this.storage = storage;
		this.showDebug = showDebug;
		storage.init();

		// Configures an early logger
		logger = new ConsoleLogger();
		logger.init(this, showDebug);

		logger.info("SecureChatClient (" + MARKER.getId() + ")");

		// Loads settings
		settings = new PropertyCollection(null);
		if (storage.doesFileExist(SETTINGS_FILE))
			settings.loadFile(storage, SETTINGS_FILE);
		saveSettings();

		// Configures the implementation factory
		implementationFactory = new ImplementationFactory(logger, settings.getPermissive(IMPLEMENTATIONS_PROPERTY));

		// Sets the default instances
		implementationFactory.set(IContext.class, this);
		implementationFactory.set(IStorage.class, storage);
		implementationFactory.set(ILogger.class, logger);
		implementationFactory.set(IImplementationFactory.class, implementationFactory);

		// Registers some default implementations
		implementationFactory.register(ConsoleLogger.MARKER, ILogger.class, ConsoleLogger::new);
		implementationFactory.register(ByteReader.MARKER, IByteReader.class, ByteReader::new);
		implementationFactory.register(ByteWriter.MARKER, IByteWriter.class, ByteWriter::new);

		// Injects into the storage
		implementationFactory.inject(storage);

		// Configures the plugin manager
		pluginManager = new PluginManager(this);

		// Loads the plugins
		pluginManager.loadPlugins();
		// Regenerate the hook cache
		pluginManager.regenerateCache();

		// Runs the early init pass
		pluginManager.invokeHook(Hooks.EarlyInit, this);
		// Reinjects into storage
		implementationFactory.inject(storage);

		// Reconfigures the logger with a new implementation
		logger = implementationFactory.provide(ILogger.class);
		// Store the instance
		implementationFactory.set(ILogger.class, logger);
		// Initialise the instance
		logger.init(this, showDebug);
		// Test the instance
		logger.debug("Logger provider: " + logger);

		// Runs the init and late init passes
		pluginManager.invokeHook(Hooks.Init, this);
		pluginManager.invokeHook(Hooks.LateInit, this);

		// Configures the GUI
		gui = implementationFactory.get(IGuiProvider.class, true);
		logger.debug("Gui provider: " + gui);

		// Flushes the settings to file
		saveSettings();

		// Configures the client manager
		IClientManager clientManager = implementationFactory.get(IClientManager.class, true);
		clientManager.init();

		// Initialises the gui
		gui.init(this::guiReady);
	}

	private void guiReady() {
		try {
			// Configures the keystore
			IKeystore keystore = implementationFactory.get(IKeystore.class, true);
			logger.info("Keystore: " + keystore);

			// Unlocks the keystore using the GUI prompt
			IKeystoreGui kgui = gui.getKeystoreGui();
			kgui.init(keystore);
			// Opens the keystore gui
			kgui.open();
			// Waits until the gui has completed
			kgui.awaitClose();

			// Configures the connection store
			IConnectionStore store = implementationFactory.get(IConnectionStore.class, true);
			logger.info("Connection Store: " + store);
			store.init();

			// Configures the network manager
			IClientNetworkManager networkManager = implementationFactory.get(IClientNetworkManager.class, true);
			logger.info("Network Manager: " + networkManager);

			// Opens the login GUI, program is now fully loaded
			gui.getLoginGui().open();
		} catch (Exception e) {
			handleCrash(e);
		}
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
		settings.saveToFile(storage, SETTINGS_FILE);
	}

	@Override
	public void handleCrash(Throwable reason) {
		// Outputs the crash log to the console
		logger.error("A crash has occured!");
		logger.error("Type: " + reason.getClass());
		logger.error("Message: " + reason.getMessage());

		// Prints a trace of all method calls upto this point
		logger.error("Crash handle trace");
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement e : stacktrace) {
			logger.error("\t" + e.getClassName() + "." + e.getMethodName() + " (" + e.getFileName() + ":"
					+ e.getLineNumber() + ")");
		}

		// Print the stacktrace of the error
		logger.error("Error Trace: ");
		stacktrace = reason.getStackTrace();
		for (StackTraceElement e : stacktrace) {
			logger.error("\t" + e.getClassName() + "." + e.getMethodName() + " (" + e.getFileName() + ":"
					+ e.getLineNumber() + ")");
		}
		if (showDebug) {
			logger.error("Internal message:");
			reason.printStackTrace(System.out);
		}

		// Displays the graphical crash GUI or quits
		if (gui != null) {
			gui.handleCrash(reason);
		} else {
			exit();
		}
	}

	@Override
	public void exit() {
		// Finds the name of the method that called the exit function
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stacktrace[2];
		// Logs the exit caller
		logger.info("Exit requested by " + element.getMethodName() + " in " + element.getClassName());

		// Saves the settigns and quits
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
	public OsType getOsType() {
		// Checks for key words in the os name
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OsType.Windows;
		} else if (osName.contains("mac")) {
			return OsType.OSX;
		} else if (osName.contains("linux") || osName.contains("nix")) {
			return OsType.Linux;
		}
		return OsType.Unknown;
	}

	@Override
	public PlatformArch getPlatformArch() {
		// Checks for numbers in the os arch
		return System.getProperty("os.arch").toLowerCase().contains("64") ? PlatformArch.X86_64 : PlatformArch.X86_32;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static void main(String[] args) {
		INSTANCE = new SecureChatClient();
		try {
			INSTANCE.init(new FileStorage(), args.length > 0 && args[0].equalsIgnoreCase("-debug"));
		} catch (Exception e) {
			INSTANCE.handleCrash(e);
		}
	}

	public static final ImplementationMarker MARKER;
	private static final CollectionProperty IMPLEMENTATIONS_PROPERTY;
	private static final String SETTINGS_FILE;
	private static SecureChatClient INSTANCE;
	static {
		MARKER = new ImplementationMarker("official", "1.0.0", "client", "1.0.0");
		IMPLEMENTATIONS_PROPERTY = new CollectionProperty("implementations");
		SETTINGS_FILE = "settings.json";
	}

}
