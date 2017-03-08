package com.securechat.api.common.storage;

import java.util.List;

import org.json.JSONObject;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.security.IEncryption;

/**
 * Provides access to permanent storage.
 */
public interface IStorage extends IImplementation {

	/**
	 * Configures the storage
	 */
	void init();

	/**
	 * Loads all plugins and provides a list of class names to attempt to load
	 * plugins from.
	 * 
	 * @return all class names
	 */
	List<String> loadPlugins();

	/**
	 * Checks whether a file exists at the given path.
	 * 
	 * @param path
	 *            the path to check
	 * @return whether the path exists
	 */
	boolean doesFileExist(String path);

	/**
	 * Loads the content of the file at the given paths and decrypts it with the
	 * given encryption if not null.
	 * 
	 * @param path
	 *            the path to load
	 * @param encryption
	 *            the encryption to use
	 * @return the read data
	 */
	IByteReader readFile(String path, IEncryption encryption);

	/**
	 * Reads the given path as a JSON object.
	 * 
	 * @param path
	 *            the path to load
	 * @return the loaded JSON object
	 */
	JSONObject readJsonFile(String path);

	/**
	 * Writes the given data with the given encryption to the path provided.
	 * 
	 * @param path
	 *            the path to write to
	 * @param encryption
	 *            the encryption to use
	 * @param data
	 *            the data to write
	 */
	void writeFile(String path, IEncryption encryption, IByteWriter data);

	/**
	 * Writes the given JSO to the path provided.
	 * 
	 * @param path
	 *            the path to write to
	 * @param obj
	 *            the JSON object to write
	 */
	void writeJsonFile(String path, JSONObject obj);

}
