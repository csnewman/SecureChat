package com.securechat.basicsecurity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.IStorage;
import com.securechat.common.plugins.Inject;
import com.securechat.common.security.IAsymmetricKeyEncryption;
import com.securechat.common.security.IKeystore;
import com.securechat.common.security.IPasswordEncryption;

public class BasicKeystore implements IKeystore {
	private static String path = "keystore.bin";
	@Inject(allowDefault = true, associate = true)
	private IPasswordEncryption passwordEncryption;
	@Inject(allowDefault = true)
	private IStorage storage;

	private boolean loaded;
	private Map<String, byte[]> asymmetricPublicKeys, asymmetricPrivateKeys;

	@Override
	public boolean generate(char[] password) {
		if (loaded) {
			throw new RuntimeException("Keystore already loaded!");
		}
		passwordEncryption.init(password);

		ByteWriter body = new ByteWriter();

		body.writeInt(asymmetricPublicKeys.size());
		for (String name : asymmetricPublicKeys.keySet()) {
			byte[] pub = asymmetricPublicKeys.get(name);
			byte[] pri = asymmetricPrivateKeys.get(name);

			body.writeString(name);

			if (pub != null) {
				body.writeBoolean(true);
				body.writeArray(pub);
			} else {
				body.writeBoolean(false);
			}

			if (pri != null) {
				body.writeBoolean(true);
				body.writeArray(pri);
			} else {
				body.writeBoolean(false);
			}
		}

		ByteWriter finalData = new ByteWriter();
		finalData.writeWriterWithChecksum(body);
		storage.writeFile(path, passwordEncryption, finalData);
		return true;
	}

	@Override
	public boolean load(char[] password) {
		if (loaded) {
			throw new RuntimeException("Keystore already loaded!");
		}
		try {
			passwordEncryption.init(password);

			asymmetricPrivateKeys = new HashMap<String, byte[]>();
			asymmetricPublicKeys = new HashMap<String, byte[]>();

			ByteReader fileData = storage.readFile(path, passwordEncryption);
			ByteReader content = fileData.readReaderWithChecksum();

			int size = content.readInt();
			for (int i = 0; i < size; i++) {
				String name = content.readString();
				byte[] pub = null, pri = null;

				if (content.readBoolean()) {
					pub = content.readArray();
				}

				if (content.readBoolean()) {
					pri = content.readArray();
				}

				asymmetricPublicKeys.put(name, pub);
				asymmetricPrivateKeys.put(name, pri);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void addAsymmetricKey(String name, byte[] publicKey, byte[] privateKey) {
		asymmetricPrivateKeys.put(name, publicKey);
		asymmetricPrivateKeys.put(name, privateKey);
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
	public void loadAsymmetricKey(String name, IAsymmetricKeyEncryption encryption) {
		encryption.load(getAsymmetricPublicKey(name), getAsymmetricPrivateKey(name));
	}

	@Override
	public boolean exists() {
		return storage.doesFileExist(path);
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public String getImplName() {
		return "official-basic_keystore";
	}

}
