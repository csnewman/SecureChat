package com.securechat.common.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import org.json.JSONException;
import org.json.JSONObject;

import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;

public class FileStorage implements IStorage {
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "file_storage",
			"1.0.0");
	private static final File baseFolder = new File("data");
	@InjectInstance
	private IImplementationFactory factory;

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
	public JSONObject readJsonFile(String path) {
		try {
			return new JSONObject(new String(Files.readAllBytes(getPath(path).toPath())));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IByteReader readFile(String path, IEncryption encryption) {
		try {
			byte[] data = Files.readAllBytes(getPath(path).toPath());

			if (encryption != null)
				data = encryption.decrypt(data);

			if (factory == null) {
				ByteReader reader = new ByteReader();
				reader.setMemoryInput(data);
				return reader;
			}

			return IByteReader.get(factory, MARKER.getId(), data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void writeJsonFile(String path, JSONObject obj) {
		try {
			FileWriter writer = new FileWriter(getPath(path));
			writer.write(obj.toString(4));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeFile(String path, IEncryption encryption, IByteWriter writer) {
		try {
			byte[] data = writer.toByteArray();
			if (encryption != null)
				data = encryption.encrypt(data);
			Files.write(getPath(path).toPath(), data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean doesFileExist(String path) {
		return getPath(path).exists();
	}

	private File getPath(String path) {
		File file = new File(path);
		if (!file.isAbsolute()) {
			return new File(baseFolder, path);
		}
		return file;
	}

	public void setImplementationFactory(IImplementationFactory factory) {
		this.factory = factory;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
