package com.securechat.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;

public class Util {

	@SuppressWarnings("unchecked")
	public static <T, B> T[] convertArray(B[] original, Class<T> type) {
		T[] newArray = (T[]) Array.newInstance(type, original.length);
		// System.arraycopy(original, 0, newArray, 0, original.length);
		for (int i = 0; i < original.length; i++) {
			newArray[i] = (T) original[i];
		}
		return newArray;
	}

	public static byte[] convertToBytes(char[] original) {
		byte[] newArray = new byte[original.length];
		for (int i = 0; i < original.length; i++) {
			newArray[i] = (byte) original[i];
		}
		return newArray;
	}

	public static char[] convertToChars(byte[] original) {
		char[] newArray = new char[original.length];
		for (int i = 0; i < original.length; i++) {
			newArray[i] = (char) original[i];
		}
		return newArray;
	}

	public static String readFileToString(File file) throws IOException {
		return new String(Files.readAllBytes(file.toPath()));
	}

}
