package com.securechat.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	
	public static char[] secureHashChars(char[] chars) {
		byte[] temp = new byte[chars.length];
		System.arraycopy(chars, 0, temp, 0, chars.length);
		temp = hashData(temp);
		char[] out = new char[temp.length];
		System.arraycopy(temp, 0, out, 0, out.length);
		return out;
	}
	
	public static String hashString(String input) {
		return new String(hashData(input.getBytes()));
	}

	// public static byte[] encryptData(byte[] data, PublicKey key) {
	// try {
	// Cipher cipher = Cipher.getInstance("RSA");
	// cipher.init(Cipher.ENCRYPT_MODE, key);
	// return cipher.doFinal(data);
	// } catch (NoSuchAlgorithmException | NoSuchPaddingException |
	// InvalidKeyException | IllegalBlockSizeException
	// | BadPaddingException e) {
	// throw new RuntimeException("Failed to encrypt data", e);
	// }
	// }
	//
	// public static byte[] decryptData(byte[] data, PrivateKey key) {
	// try {
	// Cipher cipher = Cipher.getInstance("RSA");
	// cipher.init(Cipher.ENCRYPT_MODE, key);
	// return cipher.doFinal(data);
	// } catch (NoSuchAlgorithmException | NoSuchPaddingException |
	// InvalidKeyException | IllegalBlockSizeException
	// | BadPaddingException e) {
	// throw new RuntimeException("Failed to decrypt data", e);
	// }
	// }

}
