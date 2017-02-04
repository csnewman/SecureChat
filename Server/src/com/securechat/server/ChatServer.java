package com.securechat.server;

import java.io.Console;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.security.IPasswordEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;
import com.securechat.common.FallbackLogger;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.PluginManager;
import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;
import com.securechat.common.storage.FileStorage;

public class ChatServer implements IContext {
	public static final ImplementationMarker MARKER = new ImplementationMarker("official", "1.0.0", "server", "1.0.0");
	private static final CollectionProperty IMPLEMENTATIONS_PROPERTY = new CollectionProperty("implementations");
	private static final String settingsFile = "settings.json";
	private static ChatServer INSTANCE;
	private PropertyCollection settingsCollection;
	private PluginManager pluginManager;
	private ImplementationFactory implementationFactory;
	private ILogger logger;
	private IStorage storage;
	private ServerSettings settings;
	private IAsymmetricKeyEncryption networkKey;

	// private NetworkServer networkServer;
	// private ProtectedDataStore connectionStore;
	// private UserManager userManager;

	public void init(IStorage storage, char[] keystorePassword) {
		this.storage = storage;
		storage.init();

		logger = new FallbackLogger();
		logger.init(this);

		logger.info("SecureChatServer (" + MARKER.getId() + ")");

		settingsCollection = new PropertyCollection(null);
		if (storage.doesFileExist(settingsFile))
			settingsCollection.loadFile(storage, settingsFile);
		settings = new ServerSettings(settingsCollection);
		saveSettings();

		implementationFactory = new ImplementationFactory(logger,
				settingsCollection.getPermissive(IMPLEMENTATIONS_PROPERTY));
		implementationFactory.set(IContext.class, this);
		implementationFactory.set(IStorage.class, storage);
		implementationFactory.set(IImplementationFactory.class, implementationFactory);
		implementationFactory.register(FallbackLogger.MARKER, ILogger.class, FallbackLogger::new);
		implementationFactory.register(ByteReader.MARKER, IByteReader.class, ByteReader::new);
		implementationFactory.register(ByteWriter.MARKER, IByteWriter.class, ByteWriter::new);
		implementationFactory.setFallbackDefault(ILogger.class, FallbackLogger.MARKER);
		implementationFactory.setFallbackDefault(IByteReader.class, ByteReader.MARKER);
		implementationFactory.setFallbackDefault(IByteWriter.class, ByteWriter.MARKER);

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

		IKeystore keystore = implementationFactory.get(IKeystore.class, true);
		logger.info("Keystore: " + keystore);

		saveSettings();

		if (keystore.exists()) {
			if (!keystore.load(keystorePassword)) {
				throw new RuntimeException("Invalid keystore password");
			}
		} else {
			if (!keystore.generate(keystorePassword)) {
				throw new RuntimeException("Failed to generate keystore");
			}
		}
		
		logger.info("Loading network key");
		networkKey = implementationFactory.provide(IAsymmetricKeyEncryption.class, null, true, true, "network");
		keystore.loadAsymmetricKeyOrGenerate("network", networkKey);
		
		if (settings.shouldGenerateProfile()) {
			logger.info("Generating connection profile");
			IConnectionProfileProvider provider = implementationFactory.get(IConnectionProfileProvider.class, true);
			IConnectionProfile profile = provider.generateProfileTemplate(settings.getServerName(),
					settings.getPublicIp(), settings.getPublicPort(), networkKey.getPublickey());
			IPasswordEncryption passwordEncryption = implementationFactory.provide(IPasswordEncryption.class, null,
					true, true, "connection_profile");
			passwordEncryption.init(settings.getProfilePassword().toCharArray());
			provider.saveProfileToFIle(profile, storage, "profile.sccp", passwordEncryption);
		}

		saveSettings();

		// settings = new ServerSettings();
		// settings.tryLoadAndSave();
		//
		// if (!store.keysExists(netBasePrivateKey, netBasePublicKey)) {
		// System.out.println("No network public and private key found!
		// Generaring!");
		// store.generateKeyPair(netBasePrivateKey, netBasePublicKey);
		// store.save();
		// }
		//
		// if (settings.shouldGenerateConnectionInfo()) {
		// connectionStore = new ProtectedDataStore(clientConnectionInfoFile,
		// new PasswordEncryption(
		// SecurityUtils.secureHashChars(settings.getConnectionInfoPassword().toCharArray())));
		// ByteWriter connectionInfoWriter = new ByteWriter();
		// connectionInfoWriter.writeString(settings.getServerName());
		// connectionInfoWriter.writeString(settings.getPublicIp());
		// connectionInfoWriter.writeInt(settings.getPort());
		// connectionInfoWriter
		// .writeArray(RSAEncryption.savePublicKey(store.getKey(netBasePublicKey,
		// PublicKey.class)));
		// connectionStore.setContent(connectionInfoWriter);
		// connectionStore.save();
		// }
		//
		// userManager = new UserManager(store.getOrGenKeyPair("users"));
		// userManager.tryLoadAndSave();
		// store.save();
		//
		// networkServer = new NetworkServer(this, settings.getPort());
		// networkServer.start();

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
		settingsCollection.saveToFile(storage, settingsFile);
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

		INSTANCE = new ChatServer();
		INSTANCE.init(new FileStorage(), password);
	}

}