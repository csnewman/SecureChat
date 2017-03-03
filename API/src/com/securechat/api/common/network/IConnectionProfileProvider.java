package com.securechat.api.common.network;

import java.io.IOException;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.security.IEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;
import com.securechat.api.common.storage.IStorage;

/**
 * Handles the loading and creation of connection profiles.
 */
public interface IConnectionProfileProvider extends IImplementation {

	/**
	 * Generates a new profile template with the given information ready for use
	 * by a client.
	 * 
	 * @param name
	 *            the server name
	 * @param ip
	 *            the servers ip
	 * @param port
	 *            the servers port
	 * @param publicKey
	 *            the servers public key
	 * @return the new connection profile
	 */
	IConnectionProfile generateProfileTemplate(String name, String ip, int port, byte[] publicKey);

	/**
	 * Creates a new profile based of the given template.
	 * 
	 * @param template
	 *            the profile template
	 * @param username
	 *            the username on the server
	 * @param authcode
	 *            the authcode on the server
	 * @param privateKey
	 *            the private key for networking
	 * @return the completed profile
	 */
	IConnectionProfile createProfile(IConnectionProfile template, String username, int authcode, byte[] privateKey);

	/**
	 * Loads a connection profile from the given path.
	 * 
	 * @param storage
	 *            the storage to use
	 * @param path
	 *            the location of the file
	 * @param encryption
	 *            the encryption of the file
	 * @return the loaded profile
	 * @throws IOException
	 */
	IConnectionProfile loadProfileFromFile(IStorage storage, String path, IEncryption encryption) throws IOException;

	/**
	 * Loads a connection profile directly from memory.
	 * 
	 * @param reader
	 *            the memory content
	 * @param encryption
	 *            the encryption of the data
	 * @return the loaded profile
	 */
	IConnectionProfile loadProfileFromMemory(IByteReader reader, IEncryption encryption) throws IOException;

	/**
	 * Saves a profile to a given path.
	 * 
	 * @param profile
	 *            the profile to save
	 * @param storage
	 *            the storage to use
	 * @param path
	 *            the location of the file
	 * @param encryption
	 *            the encryption of the file
	 */
	void saveProfileToFIle(IConnectionProfile profile, IStorage storage, String path, IEncryption encryption)
			throws IOException;

	/**
	 * Saves a connection profile directly to memory.
	 * 
	 * @param profile
	 *            the profile to save
	 * @param writer
	 *            the memory to save to
	 * @param encryption
	 *            the encryption of the data
	 */
	void saveProfileToMemory(IConnectionProfile profile, IByteWriter writer, IEncryption encryption) throws IOException;

	/**
	 * Returns the name of the profile format.
	 * 
	 * @return the format name
	 */
	String getDisplayName();

}
