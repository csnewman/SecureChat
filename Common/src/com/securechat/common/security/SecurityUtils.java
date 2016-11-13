package com.securechat.common.security;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SecurityUtils {

	public static byte[] hashData(byte[] input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.update(input);
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Platform unsupported!", e);
		}

	}

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(4096);
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to generate key pair", e);
		}
	}

	public static PublicKey loadPublicKey(byte[] data) {
		try {
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(data));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to load public key", e);
		}
	}

	public static PrivateKey loadPrivateKey(byte[] data) {
		try {
			return KeyFactory.getInstance("RSA").generatePrivate(new X509EncodedKeySpec(data));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to load public key", e);
		}
	}

	public static byte[] encryptData(byte[] data, PublicKey key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException("Failed to encrypt data", e);
		}
	}

	public static byte[] decryptData(byte[] data, PrivateKey key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException("Failed to decrypt data", e);
		}
	}

}
