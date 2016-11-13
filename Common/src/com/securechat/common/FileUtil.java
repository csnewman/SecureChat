package com.securechat.common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class FileUtil {

	public static String readFileToString(File file) throws IOException {
		return new String(Files.readAllBytes(file.toPath()));
	}

}
