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
import java.nio.file.StandardCopyOption;
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

/**
 * The inbuilt file based storage implementation.
 */
public class FileStorage implements IStorage {
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IImplementationFactory factory;

	@Override
	public void init() {
		// Ensure storage directories exist
		BASE_FOLDER.mkdirs();
		PLUGINS_FOLDER.mkdirs();
	}

	@Override
	public List<String> loadPlugins() {
		List<String> paths = new ArrayList<String>();

		try {
			// Gets the private add url method
			URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);

			for (File file : PLUGINS_FOLDER.listFiles()) {
				// Loads the jar file
				log.info("Loading plugin jar " + file.getPath());
				method.invoke(classLoader, file.toURI().toURL());
				paths.add(file.getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		List<String> classes = new ArrayList<String>();

		// Adds all class path items
		String javaClassPath = System.getProperty("java.class.path");
		if (javaClassPath != null) {
			paths.addAll(Arrays.asList(javaClassPath.split(File.pathSeparator)));
		}

		for (String path : paths) {
			try {
				// If the file is a jar file, load it as a plguin
				if (path.endsWith(".jar") || path.endsWith(".scplugin")) {
					JarInputStream jis = new JarInputStream(new FileInputStream(path));

					// Scans each entry in the jar
					ZipEntry entry = null;
					while ((entry = jis.getNextJarEntry()) != null) {
						if (entry.getName().endsWith(".class")) {
							String name = entry.getName();
							// Cleans the file names
							name = name.replaceAll("/", ".");
							name = name.replaceAll("\\\\", ".");
							name = name.replace(".class", "");
							classes.add(name);
						}
					}

					// Attempts to load the jars manifest
					Manifest manifest = jis.getManifest();
					if (manifest != null) {
						Attributes attributes = manifest.getMainAttributes();
						if (attributes != null) {
							// Checks for a loader class attribute
							String loaderClass = attributes.getValue("Loader-Class");
							if (loaderClass != null) {
								// Runs the loader
								runLoaderFile(loaderClass);
							}
						}
					}
					jis.close();
				} else {
					// Searches the folder
					File sDir = new File(path).getAbsoluteFile();
					String basePath = sDir.getAbsolutePath();
					List<String> found = Files.walk(Paths.get(sDir.toURI())).filter(Files::isRegularFile)
							// Filter only classes
							.filter(p -> p.toString().endsWith(".class")).map(p -> p.toFile().getAbsolutePath())
							// Cleans the file name
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
	public void installPlugin(String path) {
		try {
			// Copies plugin from path to the plugins folder, updating the plugin if needed
			File file = getPath(path);
			Files.copy(file.toPath(), new File(PLUGINS_FOLDER, file.getName()).toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void copyFile(String src, String dst) {
		try {
			Files.copy(getPath(src).toPath(), getPath(dst).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void runLoaderFile(String name) {
		log.info("Loading loader-class " + name);
		try {
			// Loads the class
			Class clazz = Class.forName(name);
			Object inst = clazz.newInstance();
			// Inject into loader class
			factory.inject(inst);
			// Calls the load function
			clazz.getDeclaredMethod("load").invoke(inst);
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | InstantiationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject readJsonFile(String path) {
		try {
			// Reads file and converts it into a json object
			return new JSONObject(new String(Files.readAllBytes(getPath(path).toPath())));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IByteReader readFile(String path, IEncryption encryption) {
		try {
			// Loads the file
			byte[] data = Files.readAllBytes(getPath(path).toPath());

			// Decrpys the data if needed
			if (encryption != null)
				data = encryption.decrypt(data);

			// Wrap the raw content with a reader
			if (factory == null) {
				ByteReader reader = new ByteReader();
				reader.setMemoryInput(data);
				return reader;
			}

			return IByteReader.get(factory, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void writeJsonFile(String path, JSONObject obj) {
		try {
			// Makes the needed folders
			File loc = getPath(path);
			loc.getParentFile().mkdirs();

			// Writes the json object to the file
			FileWriter writer = new FileWriter(loc);
			// Use 4 space tabs
			writer.write(obj.toString(4));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeFile(String path, IEncryption encryption, IByteWriter writer) {
		try {
			// Makes the needed folders
			File loc = getPath(path);
			loc.getParentFile().mkdirs();

			// Converts the writer to raw bytes
			byte[] data = writer.toByteArray();

			// Encrypts the data if needed
			if (encryption != null)
				data = encryption.encrypt(data);

			// Flushes the data to disk
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
			return new File(BASE_FOLDER, path);
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

	public static final ImplementationMarker MARKER;
	private static final File BASE_FOLDER, PLUGINS_FOLDER;
	static {
		MARKER = new ImplementationMarker("inbuilt", "n/a", "file_storage", "1.0.0");
		BASE_FOLDER = new File("data");
		PLUGINS_FOLDER = new File("plugins");
	}

}
