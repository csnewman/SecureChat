package com.securechat.javasecurity;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.security.IPasswordEncryption;

/**
 * A reference implementation of the password encryption.
 */
public class PasswordEncryption implements IPasswordEncryption {
	private SecretKey key;
	private PBEParameterSpec pbeParamSpec;
	private Cipher cipher;
	private ReentrantLock lock;

	public PasswordEncryption() {
		lock = new ReentrantLock();
	}

	@Override
	public void init(char[] password) {
		try {
			lock.lock();
			// Configures the encryption with the password
			PBEKeySpec keySpec = new PBEKeySpec(password);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			key = keyFactory.generateSecret(keySpec);
			pbeParamSpec = new PBEParameterSpec(SALT, 4096);
			cipher = Cipher.getInstance("PBEWithMD5AndDES");
			lock.unlock();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e) {
			lock.unlock();
			throw new RuntimeException("Failed to crypt data", e);
		}
	}

	@Override
	public byte[] encrypt(byte[] data) {
		try {
			lock.lock();

			// Pads data to the correct size
			int extraPadding = 8 - (data.length % 8);

			byte[] tempData = new byte[data.length + extraPadding];
			System.arraycopy(data, 0, tempData, 0, data.length);

			for (int i = data.length; i < tempData.length; i++) {
				tempData[i] = (byte) extraPadding;
			}

			// Encrypts the data
			cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
			byte[] result = cipher.doFinal(tempData);
			lock.unlock();
			return result;
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			lock.unlock();
			throw new RuntimeException("Failed to encrypt data", e);
		}
	}

	@Override
	public byte[] decrypt(byte[] data) {
		try {
			lock.lock();
			// Decrypts the data
			cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
			byte[] tempData = cipher.doFinal(data);

			// Unpads the data
			int extraPadding = tempData[tempData.length - 1];
			byte[] finalData = new byte[tempData.length - extraPadding];
			System.arraycopy(tempData, 0, finalData, 0, finalData.length);
			lock.unlock();

			return finalData;
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			lock.unlock();
			throw new RuntimeException("Failed to decrypt data", e);
		}
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	private static final byte[] SALT;
	static {
		MARKER = new ImplementationMarker(JavaSecurityPlugin.NAME, JavaSecurityPlugin.VERSION, "password_encryption",
				"1.0.0");
		SALT = new byte[] { (byte) 0x12, (byte) 0x67, (byte) 0x43, (byte) 0x32, (byte) 0x68, (byte) 0x94, (byte) 0x17,
				(byte) 0x95 };
	}

}
