package com.securechat.common.security;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.securechat.basicencryption.RSAEncryption;
import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.api.IEncryption;

public class ProtectedKeyStore extends ProtectedStore {
	private Map<String, Key> keys;

	public ProtectedKeyStore(File file, IEncryption encryptionMethod) {
		super(file, encryptionMethod);
		keys = new HashMap<String, Key>();
	}

	@Override
	protected void loadContent(ByteReader bodyReader) throws IOException {
		keys.clear();

		int keyCount = bodyReader.readInt();
		keys = new HashMap<>(keyCount);
		for (int i = 0; i < keyCount; i++) {
			String keyName = bodyReader.readString();
			int keyType = bodyReader.readByte();
			byte[] keyData = bodyReader.readArray();

			switch (keyType) {
			case 1:
				keys.put(keyName, RSAEncryption.loadPrivateKey(keyData));
				break;
			case 2:
				keys.put(keyName, RSAEncryption.loadPublicKey(keyData));
				break;
			default:
				throw new RuntimeException("Unknown key type! " + keyType);
			}
		}
	}

	@Override
	protected void writeContent(ByteWriter bodyWriter) {
		bodyWriter.writeInt(keys.size());
		for (Entry<String, Key> entry : keys.entrySet()) {
			bodyWriter.writeString(entry.getKey());
			Key key = entry.getValue();
			if (key instanceof PrivateKey) {
				bodyWriter.writeByte(1);
				bodyWriter.writeArray(RSAEncryption.savePrivateKey((PrivateKey) key));
			} else if (key instanceof PublicKey) {
				bodyWriter.writeByte(2);
				bodyWriter.writeArray(RSAEncryption.savePublicKey((PublicKey) key));
			} else {
				throw new RuntimeException("Unknown key type! " + key);
			}

		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getKey(String name, Class<T> type) {
		return (T) keys.get(name);
	}

	public boolean keysExists(String... names) {
		for (String name : names) {
			if (!keys.containsKey(name)) {
				return false;
			}
		}
		return true;
	}

	public void setKey(String name, Key key) {
		keys.put(name, key);
	}

	public KeyPair generateKeyPair(String privateName, String publicName) {
		KeyPair pair = RSAEncryption.generateKeyPair();
		setKey(privateName, pair.getPrivate());
		setKey(publicName, pair.getPublic());
		return pair;
	}

	public KeyPair generateKeyPair(String name) {
		return generateKeyPair(name + ".private", name + ".public");
	}

	public KeyPair getKeyPair(String privateName, String publicName) {
		return new KeyPair(getKey(publicName, PublicKey.class), getKey(privateName, PrivateKey.class));
	}

	public KeyPair getKeyPair(String name) {
		return getKeyPair(name + ".private", name + ".public");
	}

	public void setKeyPair(KeyPair pair, String privateName, String publicName) {
		setKey(publicName, pair.getPublic());
		setKey(privateName, pair.getPrivate());
	}

	public void setKeyPair(KeyPair pair, String name) {
		setKeyPair(pair, name + ".private", name + ".public");
	}

	public boolean keyPairExists(String privateName, String publicName) {
		return keysExists(privateName, publicName);
	}

	public boolean keyPairExists(String name) {
		return keyPairExists(name + ".private", name + ".public");
	}

	public KeyPair getOrGenKeyPair(String privateName, String publicName) {
		if (keyPairExists(privateName, publicName)) {
			return getKeyPair(privateName, publicName);
		}
		return generateKeyPair(privateName, publicName);
	}

	public KeyPair getOrGenKeyPair(String name) {
		return getOrGenKeyPair(name + ".private", name + ".public");
	}

	public void deleteKeys(String... keys) {
		for (String key : keys) {
			this.keys.remove(key);
		}
	}

}
