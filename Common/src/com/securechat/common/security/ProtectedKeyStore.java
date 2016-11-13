package com.securechat.common.security;

import java.io.File;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public class ProtectedKeyStore extends ProtectedStore {
	private Map<String, Key> keys;

	public ProtectedKeyStore(File file, String passwordHash) {
		super(file, passwordHash);
		keys = new HashMap<String, Key>();
	}

	@Override
	protected void loadContent(ByteReader bodyReader) {
		keys.clear();

		int keyCount = bodyReader.readInt();
		keys = new HashMap<>(keyCount);
		for (int i = 0; i < keyCount; i++) {
			String keyName = bodyReader.readString();
			int keyType = bodyReader.readByte();
			byte[] keyData = bodyReader.readArray();

			switch (keyType) {
			case 1:
				keys.put(keyName, SecurityUtils.loadPrivateKey(keyData));
				break;
			case 2:
				keys.put(keyName, SecurityUtils.loadPublicKey(keyData));
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
			if (key instanceof PublicKey) {
				bodyWriter.writeInt(1);
				bodyWriter.writeArray(key.getEncoded());
			} else if (key instanceof PublicKey) {
				bodyWriter.writeInt(2);
				bodyWriter.writeArray(key.getEncoded());
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
		KeyPair pair = SecurityUtils.generateKeyPair();
		setKey(privateName, pair.getPrivate());
		setKey(publicName, pair.getPublic());
		return pair;
	}

	public KeyPair loadKeyPair(String privateName, String publicName) {
		return new KeyPair(getKey(publicName, PublicKey.class), getKey(privateName, PrivateKey.class));
	}

	public void saveKeyPair(KeyPair pair, String privateName, String publicName) {
		setKey(publicName, pair.getPublic());
		setKey(privateName, pair.getPrivate());
	}

	public void deleteKeys(String... keys) {
		for (String key : keys) {
			this.keys.remove(key);
		}
	}

}
