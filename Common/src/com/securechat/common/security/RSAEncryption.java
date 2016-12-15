package com.securechat.common.security;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public class RSAEncryption implements IEncryption {
	private PublicKey pubKey;
	private PrivateKey priKey;
	private Cipher cipher;

	public RSAEncryption(KeyPair pair) {
		this(pair.getPublic(), pair.getPrivate());
	}

	public RSAEncryption(PublicKey pubKey, PrivateKey priKey) {
		this.pubKey = pubKey;
		this.priKey = priKey;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("Failed to crypt data", e);
		}
	}

	public void setPublicKey(PublicKey pubKey) {
		this.pubKey = pubKey;
	}

	public void setPrivateKey(PrivateKey priKey) {
		this.priKey = priKey;
	}

	@Override
	public byte[] encrypt(byte[] data) {
		try {
			int count = (int) Math.ceil((double) data.length / 501d);
			ByteWriter out = new ByteWriter();
			out.writeInt(data.length);

			for (int i = 0; i < count; i++) {
				int start = i * 501;
				int size = data.length - start < 501 ? data.length - start : 501;

				byte[] temp = new byte[size];
				System.arraycopy(data, start, temp, 0, size);

				cipher.init(Cipher.ENCRYPT_MODE, pubKey);
				temp = cipher.doFinal(temp);
				out.writeArray(temp);
			}

			return out.toByteArray();
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException("Failed to encrypt data", e);
		}
	}

	@Override
	public byte[] decrypt(byte[] data) {
		try {
			ByteReader in = new ByteReader(data);
			int length = in.readInt();
			int count = (int) Math.ceil((double) length / 501d);

			byte[] result = new byte[length];

			for (int i = 0; i < count; i++) {
				int start = i * 501;

				byte[] temp = in.readArray();
				cipher.init(Cipher.DECRYPT_MODE, priKey);
				temp = cipher.doFinal(temp);

				System.arraycopy(temp, 0, result, start, temp.length);
			}

			return result;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
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

	public static byte[] savePublicKey(PublicKey key) {
		return new X509EncodedKeySpec(key.getEncoded()).getEncoded();
	}

	public static PublicKey loadPublicKey(byte[] data) {
		try {
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(data));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to load public key", e);
		}
	}

	public static byte[] savePrivateKey(PrivateKey key) {
		return new PKCS8EncodedKeySpec(key.getEncoded()).getEncoded();
	}

	public static PrivateKey loadPrivateKey(byte[] data) {
		try {
			return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(data));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to load public key", e);
		}
	}
}
