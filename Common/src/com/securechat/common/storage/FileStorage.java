package com.securechat.common.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import org.json.JSONException;
import org.json.JSONObject;

import com.securechat.api.common.ILogger;
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
	private static final File pluginsFolder = new File("plugins");
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IImplementationFactory factory;

	@Override
	public void init() {
		baseFolder.mkdirs();
		pluginsFolder.mkdirs();
	}

	@Override
	public List<String> loadPlugins() {
		List<String> paths = new ArrayList<String>();

		for (File file : pluginsFolder.listFiles()) {
			log.info("Loading plugin jar " + file.getPath());
			try {
				URL url = file.toURI().toURL();
				URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
				Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
				method.setAccessible(true);
				method.invoke(classLoader, url);
				paths.add(file.getAbsolutePath());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		List<String> classes = new ArrayList<String>();

		String javaClassPath = System.getProperty("java.class.path");
		if (javaClassPath != null) {
			paths.addAll(Arrays.asList(javaClassPath.split(File.pathSeparator)));
		}

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
					Manifest manifest = jis.getManifest();
					if (manifest != null) {
						Attributes attributes = manifest.getMainAttributes();
						if (attributes != null) {
							String loaderClass = attributes.getValue("Loader-Class");
							if (loaderClass != null) {
								runLoaderFile(loaderClass);
							}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void runLoaderFile(String name) {
		log.info("Loading loader-class " + name);
		try {
			Class clazz = Class.forName(name);
			Object inst = clazz.newInstance();
			factory.inject(inst);
			clazz.getDeclaredMethod("load").invoke(inst);
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | InstantiationException e) {
			e.printStackTrace();
		}
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
			System.out.println("readbytes");
			byte[] data = Files.readAllBytes(getPath(path).toPath());

			System.out.println("decrypt");
			if (encryption != null)
				data = encryption.decrypt(data);

			System.out.println("reader");

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
			File loc = getPath(path);
			loc.getParentFile().mkdirs();

			FileWriter writer = new FileWriter(loc);
			writer.write(obj.toString(4));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeFile(String path, IEncryption encryption, IByteWriter writer) {
		try {
			File loc = getPath(path);
			loc.getParentFile().mkdirs();

			byte[] data = writer.toByteArray();
			if (encryption != null)
				data = encryption.encrypt(data);
			Files.write(loc.toPath(), data);
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
