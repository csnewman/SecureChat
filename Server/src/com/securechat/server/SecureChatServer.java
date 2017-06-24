package com.securechat.server;

import java.io.Console;
import java.io.IOException;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.OsType;
import com.securechat.api.common.PlatformArch;
import com.securechat.api.common.Sides;
import com.securechat.api.common.database.IDatabase;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PrimitiveProperty;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.security.IPasswordEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;
import com.securechat.api.server.IServerManager;
import com.securechat.api.server.network.IServerNetworkManager;
import com.securechat.api.server.users.IUserManager;
import com.securechat.common.ConsoleLogger;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.PluginManager;
import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;
import com.securechat.common.storage.FileStorage;

public class SecureChatServer implements IContext {
	private PropertyCollection settingsCollection;
	private PluginManager pluginManager;
	private ImplementationFactory implementationFactory;
	private ILogger logger;
	private IStorage storage;
	private IAsymmetricKeyEncryption networkKey;
	private IServerNetworkManager networkManager;
	private boolean showDebug;

	public void init(IStorage storage, boolean showDebug, char[] keystorePassword) throws IOException {
		this.storage = storage;
		storage.init();
		this.showDebug = showDebug;

		// Configures an early logger
		logger = new ConsoleLogger();
		logger.init(this, showDebug);

		logger.info("SecureChatServer (" + MARKER.getId() + ")");

		// Loads settings
		settingsCollection = new PropertyCollection(null);
		if (storage.doesFileExist(SETTINGS_FILE))
			settingsCollection.loadFile(storage, SETTINGS_FILE);
		PropertyCollection serverCollection = settingsCollection.getPermissive(SERVER_PROPERTY);
		saveSettings();

		// Configures the implementation factory
		implementationFactory = new ImplementationFactory(logger,
				settingsCollection.getPermissive(IMPLEMENTATIONS_PROPERTY));
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
		pluginManager.regenerateCache();

		// Runs the early init pass
		pluginManager.invokeHook(Hooks.EarlyInit, this);
		// Reinjects into storage
		implementationFactory.inject(storage);

		// Reconfigures the logger with a new implementation
		logger = implementationFactory.provide(ILogger.class);
		implementationFactory.set(ILogger.class, logger);
		logger.init(this, showDebug);
		logger.debug("Logger provider: " + logger);

		// Runs the init
		pluginManager.invokeHook(Hooks.Init, this);

		// Configures the database
		IDatabase database = implementationFactory.get(IDatabase.class, true);
		database.init();
		logger.debug("Database: " + database);

		// Configures the user manager
		IUserManager userManager = implementationFactory.get(IUserManager.class, true);
		userManager.init();
		logger.debug("User Manager: " + userManager);

		// Runs the late init
		pluginManager.invokeHook(Hooks.LateInit, this);

		// Configures the keystore
		IKeystore keystore = implementationFactory.get(IKeystore.class, true);
		logger.info("Keystore: " + keystore);

		// Flushes the settings to file
		saveSettings();

		// Loads the keystore
		if (keystore.exists()) {
			if (!keystore.load(keystorePassword)) {
				throw new RuntimeException("Invalid keystore password");
			}
		} else {
			if (!keystore.generate(keystorePassword)) {
				throw new RuntimeException("Failed to generate keystore");
			}
		}

		// Loads the network key
		logger.info("Loading network key");
		networkKey = implementationFactory.provide(IAsymmetricKeyEncryption.class);
		keystore.loadAsymmetricKeyOrGenerate("network", networkKey);

		// Configures the network manager
		logger.info("Loading network manager");
		networkManager = implementationFactory.get(IServerNetworkManager.class, true);
		networkManager.init(networkKey);

		// Generates the connection profile
		PropertyCollection profileCollection = serverCollection.getPermissive(PROFILE_PROPERTY);
		if (profileCollection.get(GENERATE_PROPERY)) {
			logger.info("Generating connection profile");
			IConnectionProfileProvider provider = implementationFactory.get(IConnectionProfileProvider.class, true);
			IConnectionProfile profile = networkManager.generateProfile(provider);
			IPasswordEncryption passwordEncryption = implementationFactory.provide(IPasswordEncryption.class);
			passwordEncryption.init(profileCollection.get(PASSWORD_PROPERY).toCharArray());
			provider.saveProfileToFIle(profile, storage, "profile.sccp", passwordEncryption);
		}

		// Flushes the settings to file
		saveSettings();

		// Starts the network
		networkManager.start();

		// Configures the server manager
		IServerManager manager = implementationFactory.get(IServerManager.class, true);
		manager.init();
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
		return settingsCollection;
	}

	@Override
	public void saveSettings() {
		settingsCollection.saveToFile(storage, SETTINGS_FILE);
	}

	@Override
	public void handleCrash(Throwable reason) {
		logger.error("A crash has occured!");
		logger.error("Type: " + reason.getClass());
		logger.error("Message: " + reason.getMessage());

		logger.error("Crash handle trace");
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement e : stacktrace) {
			logger.error("\t" + e.getClassName() + "." + e.getMethodName() + " (" + e.getFileName() + ":"
					+ e.getLineNumber() + ")");
		}

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
		exit();
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
		return Sides.Server;
	}

	@Override
	public String getAppName() {
		return "official-server";
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
		Console console = System.console();
		char[] password;
		if (console == null) {
			System.out.println("No console found! Using default 'test' password for master keystore");
			password = new char[] { 't', 'e', 's', 't' };
		} else {
			password = console.readPassword("Master Keystore Password: ");
		}

		INSTANCE = new SecureChatServer();
		try {
			INSTANCE.init(new FileStorage(), args.length > 0 && args[0].equalsIgnoreCase("-debug"), password);
		} catch (Exception e) {
			INSTANCE.handleCrash(e);
		}
	}

	public static final ImplementationMarker MARKER;
	private static final PrimitiveProperty<Boolean> GENERATE_PROPERY;
	private static final PrimitiveProperty<String> PASSWORD_PROPERY;
	private static final CollectionProperty PROFILE_PROPERTY, SERVER_PROPERTY, IMPLEMENTATIONS_PROPERTY;
	private static final String SETTINGS_FILE;
	private static SecureChatServer INSTANCE;
	static {
		MARKER = new ImplementationMarker("official", "1.0.0", "server", "1.0.0");
		GENERATE_PROPERY = new PrimitiveProperty<Boolean>("generate", true);
		PASSWORD_PROPERY = new PrimitiveProperty<String>("password", "unset");
		PROFILE_PROPERTY = new CollectionProperty("profile", GENERATE_PROPERY, PASSWORD_PROPERY);
		SERVER_PROPERTY = new CollectionProperty("server", PROFILE_PROPERTY);
		IMPLEMENTATIONS_PROPERTY = new CollectionProperty("implementations");
		SETTINGS_FILE = "settings.json";
	}

}