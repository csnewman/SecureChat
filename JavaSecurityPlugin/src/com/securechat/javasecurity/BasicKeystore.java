package com.securechat.javasecurity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.Inject;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.security.IPasswordEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;

/**
 * A reference implementation of the keystore.
 */
public class BasicKeystore implements IKeystore {
	@InjectInstance
	private ILogger log;
	@Inject
	private IPasswordEncryption passwordEncryption;
	@InjectInstance
	private IStorage storage;
	@InjectInstance
	private IImplementationFactory factory;

	private boolean loaded;
	private Map<String, byte[]> asymmetricPublicKeys, asymmetricPrivateKeys;

	@Override
	public boolean generate(char[] password) {
		if (loaded) {
			throw new RuntimeException("Keystore already loaded!");
		}
		log.debug("Creating keystore");
		// Initialise encryption with the password
		passwordEncryption.init(password);

		asymmetricPrivateKeys = new HashMap<String, byte[]>();
		asymmetricPublicKeys = new HashMap<String, byte[]>();

		save();

		loaded = true;
		return true;
	}

	private void save() {
		log.debug("Saving keystore");
		try {
			IByteWriter body = IByteWriter.get(factory);

			// Writes all of the keys
			body.writeInt(asymmetricPublicKeys.size());
			for (String name : asymmetricPublicKeys.keySet()) {
				byte[] pub = asymmetricPublicKeys.get(name);
				byte[] pri = asymmetricPrivateKeys.get(name);

				// Writes the keyname
				body.writeString(name);

				// Writes the public key
				if (pub != null) {
					body.writeBoolean(true);
					body.writeArray(pub);
				} else {
					body.writeBoolean(false);
				}

				// Writes the private key
				if (pri != null) {
					body.writeBoolean(true);
					body.writeArray(pri);
				} else {
					body.writeBoolean(false);
				}
			}

			// Wraps the data with a checksum
			IByteWriter finalData = IByteWriter.get(factory);
			finalData.writeWriterWithChecksum(body);
			// Flushes the data to disk
			storage.writeFile(PATH, passwordEncryption, finalData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean load(char[] password) {
		if (loaded) {
			throw new RuntimeException("Keystore already loaded!");
		}
		log.debug("Loading keystore");
		try {
			// Initialise encryption with the password
			passwordEncryption.init(password);

			asymmetricPrivateKeys = new HashMap<String, byte[]>();
			asymmetricPublicKeys = new HashMap<String, byte[]>();

			// Load data from disk
			IByteReader fileData = storage.readFile(PATH, passwordEncryption);
			if (fileData == null) {
				return false;
			}

			IByteReader content = fileData.readReaderWithChecksum();

			// Loads each key
			int size = content.readInt();
			for (int i = 0; i < size; i++) {
				String name = content.readString();
				byte[] pub = null, pri = null;

				// Read public key
				if (content.readBoolean()) {
					pub = content.readArray();
				}

				// Read private key
				if (content.readBoolean()) {
					pri = content.readArray();
				}

				asymmetricPublicKeys.put(name, pub);
				asymmetricPrivateKeys.put(name, pri);
			}
			loaded = true;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void addAsymmetricKey(String name, byte[] publicKey, byte[] privateKey) {
		asymmetricPublicKeys.put(name, publicKey);
		asymmetricPrivateKeys.put(name, privateKey);
		save();
	}

	@Override
	public void addAsymmetricKey(String name, IAsymmetricKeyEncryption encryption) {
		addAsymmetricKey(name, encryption.getPublickey(), encryption.getPrivatekey());
	}

	@Override
	public byte[] getAsymmetricPublicKey(String name) {
		return asymmetricPublicKeys.get(name);
	}

	@Override
	public byte[] getAsymmetricPrivateKey(String name) {
		return asymmetricPrivateKeys.get(name);
	}

	@Override
	public void loadAsymmetricKey(String name, IAsymmetricKeyEncryption encryption) throws IOException {
		encryption.load(getAsymmetricPublicKey(name), getAsymmetricPrivateKey(name));
	}

	@Override
	public void loadAsymmetricKeyOrGenerate(String name, IAsymmetricKeyEncryption encryption) throws IOException {
		if (hasAsymmetricKey(name))
			loadAsymmetricKey(name, encryption);
		else {
			encryption.generate();
			addAsymmetricKey(name, encryption);
		}
	}

	@Override
	public boolean hasAsymmetricPublicKey(String name) {
		return asymmetricPublicKeys.containsKey(name);
	}

	@Override
	public boolean hasAsymmetricPrivateKey(String name) {
		return asymmetricPrivateKeys.containsKey(name);
	}

	@Override
	public boolean hasAsymmetricKey(String name) {
		return hasAsymmetricPublicKey(name) && hasAsymmetricPrivateKey(name);
	}

	@Override
	public boolean exists() {
		return storage.doesFileExist(PATH);
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	private static String PATH;
	static {
		MARKER = new ImplementationMarker(JavaSecurityPlugin.NAME, JavaSecurityPlugin.VERSION, "basic_keystore",
				"1.0.0");
		PATH = "keystore.bin";
	}

}
