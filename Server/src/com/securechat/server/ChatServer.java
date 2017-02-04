package com.securechat.server;

import java.io.Console;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;
import com.securechat.common.FallbackLogger;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.PluginManager;
import com.securechat.common.storage.ByteReader;
import com.securechat.common.storage.ByteWriter;
import com.securechat.common.storage.FileStorage;

public class ChatServer implements IContext{
//	public static final String netBasePrivateKey = "netBasePrivate", netBasePublicKey = "netBasePublic";
//	private static final File clientConnectionInfoFile = new File("clientConnectionInfo.scci");
	public static final ImplementationMarker MARKER = new ImplementationMarker("official", "1.0.0", "server",
			"1.0.0");
	public static final CollectionProperty implementationsProp = new CollectionProperty("implementations");
	private static final String settingsFile = "settings.json";
	private static ChatServer INSTANCE;
	private PropertyCollection settings;
	private PluginManager pluginManager;
	private ImplementationFactory implementationFactory;
	private ILogger logger;
	private IStorage storage;
	
//	private NetworkServer networkServer;
//	private ServerSettings settings;
//	private ProtectedKeyStore store;
//	private ProtectedDataStore connectionStore;
//	private UserManager userManager;

	public void init(IStorage storage) {
		this.storage = storage;
		storage.init();
		
		logger = new FallbackLogger();
		logger.init(this);
		
		logger.info("SecureChatServer ("+MARKER.getId()+")");

		settings = new PropertyCollection(null);
		if (storage.doesFileExist(settingsFile))
			settings.loadFile(storage, settingsFile);
		saveSettings();
		
		implementationFactory = new ImplementationFactory(logger, settings.getPermissive(implementationsProp));
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

		saveSettings();
		
		
		
		System.out.println("SecureChat Server 1.0");
		Console console = System.console();
		char[] password;
		if (console == null) {
			System.out.println("No console found! Using default 'test' password for master keystore");
			password = new char[] { 't', 'e', 's', 't' };
		} else {
			password = console.readPassword("Master Keystore Password: ");
		}

//		store = new ProtectedKeyStore(new File("data.pstore"),
//				new PasswordEncryption(SecurityUtils.secureHashChars(password)));
//		store.tryLoadAndSave();
//
//		settings = new ServerSettings();
//		settings.tryLoadAndSave();
//
//		if (!store.keysExists(netBasePrivateKey, netBasePublicKey)) {
//			System.out.println("No network public and private key found! Generaring!");
//			store.generateKeyPair(netBasePrivateKey, netBasePublicKey);
//			store.save();
//		}
//
//		if (settings.shouldGenerateConnectionInfo()) {
//			connectionStore = new ProtectedDataStore(clientConnectionInfoFile, new PasswordEncryption(
//					SecurityUtils.secureHashChars(settings.getConnectionInfoPassword().toCharArray())));
//			ByteWriter connectionInfoWriter = new ByteWriter();
//			connectionInfoWriter.writeString(settings.getServerName());
//			connectionInfoWriter.writeString(settings.getPublicIp());
//			connectionInfoWriter.writeInt(settings.getPort());
//			connectionInfoWriter
//					.writeArray(RSAEncryption.savePublicKey(store.getKey(netBasePublicKey, PublicKey.class)));
//			connectionStore.setContent(connectionInfoWriter);
//			connectionStore.save();
//		}
//
//		userManager = new UserManager(store.getOrGenKeyPair("users"));
//		userManager.tryLoadAndSave();
//		store.save();
//
//		networkServer = new NetworkServer(this, settings.getPort());
//		networkServer.start();

	}
//
//	public ProtectedKeyStore getStore() {
//		return store;
//	}
//
//	public UserManager getUserManager() {
//		return userManager;
//	}


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
		INSTANCE = new ChatServer();
		INSTANCE.init(new FileStorage());
	}

}