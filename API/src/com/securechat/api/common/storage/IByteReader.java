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
	void setMemoryInput(byte[] data);

	/**
	 * Sets the input stream to use directly.
	 * 
	 * @param stream
	 */
	void setInput(InputStream stream);

	/**
	 * Reads a byte from the stream.
	 * 
	 * @return the byte read
	 * @throws IOException
	 *             if an error occurred
	 */
	int readByte() throws IOException;

	/**
	 * Reads an enum from the stream.
	 * 
	 * @param type
	 *            the enum type
	 * @return the enum read
	 * @throws IOException
	 *             if an error occurred
	 */
	<T extends Enum<T>> T readEnum(Class<T> type) throws IOException;

	/**
	 * Reads an int from the stream.
	 * 
	 * @return the read int
	 * @throws IOException
	 *             if an error occured
	 */
	int readInt() throws IOException;

	/**
	 * Reads a long from the stream.
	 * 
	 * @return the read long
	 * @throws IOException
	 */
	long readLong() throws IOException;

	/**
	 * Reads a string from the stream.
	 * 
	 * @return the read string
	 * @throws IOException
	 *             if an error occurred
	 */
	String readString() throws IOException;

	/**
	 * Reads a string from the stream with support for null values.
	 * 
	 * @return the read string
	 * @throws IOException
	 *             if an error occurred.
	 */
	String readStringWithNull() throws IOException;

	/**
	 * Reads a boolean from the stream.
	 * 
	 * @return the read boolean
	 * @throws IOException
	 *             if an error occurred
	 */
	boolean readBoolean() throws IOException;

	/**
	 * Reads an array from the stream.
	 * 
	 * @return the read array
	 * @throws IOException
	 *             if an error occurred
	 */
	byte[] readArray() throws IOException;

	/**
	 * Reads an array from the stream with support for null.
	 * 
	 * @return the read array
	 * @throws IOException
	 *             if an error occurred
	 */
	byte[] readArrayWithNull() throws IOException;

	/**
	 * Reads an array of fixed size directly from the stream.
	 * 
	 * @param size
	 *            the size of the array to read
	 * @return the read array
	 * @throws IOException
	 *             if an error occurred.
	 */
	byte[] readFixedArray(int size) throws IOException;

	/**
	 * Reads an array and creates a byte reader with its content.
	 * 
	 * @return the byte reader for the array
	 * @throws IOException
	 *             if an error occurred.
	 */
	IByteReader readReaderContent() throws IOException;

	/**
	 * Reads an array and creates a byte reader with its content. The contents
	 * are first checked against the checksum.
	 * 
	 * @return the byte reader for the array
	 * @throws IOException
	 *             if an error occurred.
	 */
	IByteReader readReaderWithChecksum() throws IOException;

	/**
	 * Gets the size of the input stream.
	 * 
	 * @return the available bytes
	 */
	int getSize();

	/**
	 * Closes the input stream.
	 * 
	 * @throws IOException
	 *             if an error occured.
	 */
	void close() throws IOException;

	/**
	 * Returns the raw byte data used when constructing the memory input stream.
	 * 
	 * @return
	 */
	byte[] getRawData();

	/**
	 * A helper method that automatically gets a byte reader and sets its
	 * content.
	 * 
	 * @param factory
	 *            the implementation factory to use
	 * @param data
	 *            the content to use
	 * @return the created byte reader
	 */
	public static IByteReader get(IImplementationFactory factory, byte[] data) {
		IByteReader reader = factory.provide(IByteReader.class);
		reader.setMemoryInput(data);
		return reader;
	}

	/**
	 * A helper method that automatically gets a byte reader and sets its
	 * content.
	 * 
	 * @param factory
	 *            the implementation factory to use
	 * @param stream
	 *            the content to use
	 * @return the created reader
	 */
	public static IByteReader get(IImplementationFactory factory, InputStream stream) {
		IByteReader reader = factory.provide(IByteReader.class);
		reader.setInput(stream);
		return reader;
	}

}
