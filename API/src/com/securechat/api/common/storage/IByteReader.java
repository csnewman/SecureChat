package com.securechat.api.common.storage;

import java.io.IOException;
import java.io.InputStream;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;

/**
 * Reads objects directly from an input stream.
 */
public interface IByteReader extends IImplementation {

	/**
	 * Creates an in memory input stream of the given bytes.
	 * 
	 * @param data
	 *            the data to read from
	 */
	public void setMemoryInput(byte[] data);

	/**
	 * Sets the input stream to use directly.
	 * 
	 * @param stream
	 */
	public void setInput(InputStream stream);

	/**
	 * Reads a byte from the stream.
	 * 
	 * @return the byte read
	 * @throws IOException
	 *             if an error occurred 
	 */
	public int readByte() throws IOException;

	/**
	 * Reads an enum from the stream.
	 * 
	 * @param type
	 *            the enum type
	 * @return the enum read
	 * @throws IOException
	 *             if an error occurred
	 */
	public <T extends Enum<T>> T readEnum(Class<T> type) throws IOException;

	/**
	 * Reads an int from the stream.
	 * 
	 * @return the read int
	 * @throws IOException
	 *             if an error occured
	 */
	public int readInt() throws IOException;

	/**
	 * Reads a long from the stream.
	 * 
	 * @return the read long
	 * @throws IOException
	 */
	public long readLong() throws IOException;

	/**
	 * Reads a string from the stream.
	 * 
	 * @return the read string
	 * @throws IOException
	 *             if an error occurred
	 */
	public String readString() throws IOException;

	/**
	 * Reads a string from the stream with support for null values.
	 * 
	 * @return the read string
	 * @throws IOException
	 *             if an error occurred.
	 */
	public String readStringWithNull() throws IOException;

	/**
	 * Reads a boolean from the stream.
	 * 
	 * @return the read boolean
	 * @throws IOException
	 *             if an error occurred
	 */
	public boolean readBoolean() throws IOException;

	/**
	 * Reads an array from the stream.
	 * 
	 * @return the read array
	 * @throws IOException
	 *             if an error occurred
	 */
	public byte[] readArray() throws IOException;

	/**
	 * Reads an array from the stream with support for null.
	 * 
	 * @return the read array
	 * @throws IOException
	 *             if an error occurred
	 */
	public byte[] readArrayWithNull() throws IOException;

	/**
	 * Reads an array of fixed size directly from the stream.
	 * 
	 * @param size
	 *            the size of the array to read
	 * @return the read array
	 * @throws IOException
	 *             if an error occurred.
	 */
	public byte[] readFixedArray(int size) throws IOException;

	/**
	 * Reads an array and creates a byte reader with its content.
	 * 
	 * @return the byte reader for the array
	 * @throws IOException
	 *             if an error occurred.
	 */
	public IByteReader readReaderContent() throws IOException;

	/**
	 * Reads an array and creates a byte reader with its content. The contents
	 * are first checked against the checksum.
	 * 
	 * @return the byte reader for the array
	 * @throws IOException
	 *             if an error occurred.
	 */
	public IByteReader readReaderWithChecksum() throws IOException;

	/**
	 * Gets the size of the input stream.
	 * 
	 * @return the available bytes
	 */
	public int getSize();

	/**
	 * Closes the input stream.
	 * 
	 * @throws IOException
	 *             if an error occured.
	 */
	public void close() throws IOException;

	/**
	 * Returns the raw byte data used when constructing the memory input stream.
	 * 
	 * @return
	 */
	public byte[] getRawData();

	/**
	 * A helper function that automatically gets a byte reader and sets its
	 * content.
	 * 
	 * @param factory
	 *            the implementation factory to use
	 * @param name
	 *            the name to associate the used
	 * @param data
	 *            the content to use
	 * @return the created byte reader
	 */
	public static IByteReader get(IImplementationFactory factory, String name, byte[] data) {
		IByteReader reader = factory.provide(IByteReader.class, new ImplementationMarker[0], true, true, name);
		reader.setMemoryInput(data);
		return reader;
	}

	/**
	 * A helper function that automatically gets a byte reader and sets its
	 * content.
	 * 
	 * @param factory
	 *            the implementation factory to use
	 * @param name
	 *            the name to associate the used
	 * @param stream
	 *            the content to use
	 * @return the created reader
	 */
	public static IByteReader get(IImplementationFactory factory, String name, InputStream stream) {
		IByteReader reader = factory.provide(IByteReader.class, new ImplementationMarker[0], true, true, name);
		reader.setInput(stream);
		return reader;
	}

}
