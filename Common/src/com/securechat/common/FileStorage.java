package com.securechat.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import com.securechat.common.security.IEncryption;

public class FileStorage implements IStorage {
	private static final File baseFolder = new File("data");

	@Override
	public void init() {
		baseFolder.mkdirs();
	}
	
	@Override
	public List<String> loadPlugins() {
		// TODO: Load plugin files
		
		List<String> classes = new ArrayList<String>();

		String javaClassPath = System.getProperty("java.class.path");
		if (javaClassPath == null) {
			return classes;
		}

		List<String> paths = Arrays.asList(javaClassPath.split(File.pathSeparator));
		for (String path : paths) {
			try {
				if (path.endsWith(".jar")) {
					JarInputStream jis = new JarInputStream(new FileInputStream(path));
					ZipEntry entry = null;
					while ((entry = jis.getNextJarEntry()) != null) {
						if (entry.getName().endsWith(".class")) {
							String name = entry.getName();
							name = name.replaceAll("/", ".");
							name = name.replaceAll("\\\\", ".");
							name = name.replace(".class", "");
							classes.add(name);
						}
					}
					jis.close();
				} else {
					File sDir = new File(path).getAbsoluteFile();
					String basePath = sDir.getAbsolutePath();
					List<String> found = Files.walk(Paths.get(sDir.toURI())).filter(Files::isRegularFile)
							.filter(p -> p.toString().endsWith(".class")).map(p -> p.toFile().getAbsolutePath())
							.map(s -> s.substring(basePath.length() + 1))
							.map(s -> s.replaceAll("/", ".").replaceAll("\\\\", ".").replace(".class", ""))
							.collect(Collectors.toList());
					classes.addAll(found);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return classes;
	}

	@Override
	public ByteReader readFile(String path, IEncryption encryption) {
		try {
			File file = new File(baseFolder, path);
			return new ByteReader(encryption.decrypt(Files.readAllBytes(file.toPath())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void writeFile(String path, IEncryption encryption, ByteWriter data) {
		try {
			File file = new File(baseFolder, path);
			Files.write(file.toPath(), encryption.encrypt(data.toByteArray()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean doesFileExist(String path) {
		return new File(baseFolder, path).exists();
	}

	@Override
	public String getImplName() {
		return "official-file_storage";
	}

}
