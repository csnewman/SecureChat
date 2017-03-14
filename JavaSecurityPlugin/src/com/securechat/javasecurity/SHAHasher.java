package com.securechat.javasecurity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.security.IHasher;

/**
 * A reference implementation of the SHA hasher.
 */
public class SHAHasher implements IHasher {

	@Override
	public byte[] hashData(byte[] input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.update(input);
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Platform unsupported!", e);
		}
	}

	@Override
	public char[] hashChars(char[] chars) {
		// Converts the chars to bytes
		byte[] bytes = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			bytes[i] = (byte) chars[i];
		}

		// Hashes the bytes
		byte[] hash = hashData(bytes);

		// Converts the hash to a string
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			buff.append(Integer.toHexString(0xff & hash[i]));
		}

		// Converts the string back to a char array
		char[] out = new char[buff.length()];
		buff.getChars(0, buff.length(), out, 0);
		return out;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker(JavaSecurityPlugin.NAME, JavaSecurityPlugin.VERSION, "sha_hasher", "1.0.0");
	}

}
