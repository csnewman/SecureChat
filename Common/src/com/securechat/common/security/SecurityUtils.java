package com.securechat.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.securechat.common.Util;

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
		byte[] hash = hashData(Util.convertToBytes(chars));
		StringBuffer buff = new StringBuffer();
		for(int i = 0; i < hash.length; i++){
			buff.append(Integer.toHexString(0xff & hash[i]));
		}
		char[] out = new char[buff.length()];
		buff.getChars(0, buff.length(), out, 0);
		return out;
	}
	
}
