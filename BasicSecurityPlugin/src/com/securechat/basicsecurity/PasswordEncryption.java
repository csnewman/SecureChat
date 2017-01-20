package com.securechat.basicsecurity;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.securechat.api.common.security.IPasswordEncryption;

public class PasswordEncryption implements IPasswordEncryption {
	private static final byte[] salt = { (byte) 0x12, (byte) 0x67, (byte) 0x43, (byte) 0x32, (byte) 0x68, (byte) 0x94,
			(byte) 0x17, (byte) 0x95 };
	private SecretKey key;
	private PBEParameterSpec pbeParamSpec;
	private Cipher cipher;

	@Override
	public void init(char[] password) {
		try {
			PBEKeySpec keySpec = new PBEKeySpec(password);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			key = keyFactory.generateSecret(keySpec);
			pbeParamSpec = new PBEParameterSpec(salt, 4096);
			cipher = Cipher.getInstance("PBEWithMD5AndDES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e) {
			throw new RuntimeException("Failed to crypt data", e);
		}
	}

	@Override
	public byte[] encrypt(byte[] data) {
		try {
			int extraPadding = 8 - (data.length % 8);

			byte[] tempData = new byte[data.length + extraPadding];
			System.arraycopy(data, 0, tempData, 0, data.length);

			for (int i = data.length; i < tempData.length; i++) {
				tempData[i] = (byte) extraPadding;
			}

			cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
			return cipher.doFinal(tempData);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException("Failed to encrypt data", e);
		}
	}

	@Override
	public byte[] decrypt(byte[] data) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
			byte[] tempData = cipher.doFinal(data);
			int extraPadding = tempData[tempData.length - 1];
			byte[] finalData = new byte[tempData.length - extraPadding];
			System.arraycopy(tempData, 0, finalData, 0, finalData.length);
			return finalData;
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException("Failed to decrypt data", e);
		}
	}

	public String getImplName() {
		return "official-password_encryption";
	}

}
