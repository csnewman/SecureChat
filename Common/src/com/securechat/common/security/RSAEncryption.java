package com.securechat.common.security;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAEncryption implements IEncryption {
	private PublicKey pubKey;
	private PrivateKey priKey;

	public RSAEncryption(KeyPair pair) {
		this(pair.getPublic(), pair.getPrivate());
	}

	public RSAEncryption(PublicKey pubKey, PrivateKey priKey) {
		this.pubKey = pubKey;
		this.priKey = priKey;
	}

	@Override
	public byte[] encrypt(byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException("Failed to encrypt data", e);
		}
	}

	@Override
	public byte[] decrypt(byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, priKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException("Failed to decrypt data", e);
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
}
