package com.securechat.api.common.storage;

import java.io.IOException;
import java.io.OutputStream;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;

/**
 * Writes objects directly to an input stream.
 */
public interface IByteWriter extends IImplementation {

	/**
	 * Creates an in memory output stream.
	 */
	void setMemoryOutput();

	/**
	 * Sets the output stream
	 * 
	 * @param stream
	 *            the stream to output to
	 */
	void setOutput(OutputStream stream);

	/**
	 * Writes a byte to the stream.
	 * 
	 * @param i
	 *            the byte to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeByte(int i) throws IOException;

	/**
	 * Writes an enum to the stream.
	 * 
	 * @param e
	 *            the enum to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeEnum(Enum<?> e) throws IOException;

	/**
	 * Writes an integer to the stream.
	 * 
	 * @param i
	 *            the int to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeInt(int i) throws IOException;

	/**
	 * Writes a long to the stream.
	 * 
	 * @param l
	 *            the long to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeLong(long l) throws IOException;

	/**
	 * Writes a string to the stream.
	 * 
	 * @param str
	 *            the string to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeString(String str) throws IOException;

	/**
	 * Writes a string to the stream with support for null.
	 * 
	 * @param str
	 *            the string to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeStringWithNull(String str) throws IOException;

	/**
	 * Writes a boolean to the stream.
	 * 
	 * @param bool
	 *            the boolean to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeBoolean(boolean bool) throws IOException;

	/**
	 * Writes a byte array to the stream.
	 * 
	 * @param data
	 *            the array to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeArray(byte[] data) throws IOException;

	/**
	 * Writes a byte array to the stream with support for null.
	 * 
	 * @param data
	 *            the array to write
	 * @throws IOException
	 *             if an error occurred.
	 */
	void writeArrayWithNull(byte[] data) throws IOException;

	/**
	 * Writes the content of the array directly to the stream.
	 * 
	 * @param data
	 *            the data to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeFixedArray(byte[] data) throws IOException;

	/**
	 * Writes the content of the writer to the stream.
	 * 
	 * @param writer
	 *            the content to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeWriterContent(IByteWriter writer) throws IOException;

	/**
	 * Writes the content of the writer to the stream along with a hash.
	 * 
	 * @param writer
	 *            the content to write
	 * @throws IOException
	 *             if an error occurred
	 */
	void writeWriterWithChecksum(IByteWriter writer) throws IOException;

	/**
	 * Returns the byte data stored in the in memory byte stream.
	 * 
	 * @return the in memory byte data
	 */
	byte[] toByteArray();

	/**
	 * Closes the stream.
	 */
	void close();

	/**
	 * A helper method that automatically fetches a byte writer and sets it to
	 * an in memory output.
	 * 
	 * @param factory
	 *            the implementation factory to use
	 * @param name
	 *            the name to associate the used writer to
	 * @return the created writer
	 */
	public static IByteWriter get(IImplementationFactory factory, String name) {
		IByteWriter writer = factory.provide(IByteWriter.class, new ImplementationMarker[0], true, true, name);
		writer.setMemoryOutput();
		return writer;
	}

	/**
	 * A helper method that automatically fetches a byte writer and sets its
	 * output stream.
	 * 
	 * @param factory
	 *            the implementation factory to use
	 * @param name
	 *            the name to associate the used writer to
	 * @param stream
	 *            the output stream
	 * @return the created writer
	 */
	public static IByteWriter get(IImplementationFactory factory, String name, OutputStream stream) {
		IByteWriter writer = factory.provide(IByteWriter.class, new ImplementationMarker[0], true, true, name);
		writer.setOutput(stream);
		return writer;
	}

}
