package com.securechat.api.common.storage;

import java.io.OutputStream;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;

public interface IByteWriter extends IImplementation{

	public void setMemoryOutput();
	
	public void setStream(OutputStream stream);
	
	public void writeByte(int i);

	public void writeEnum(Enum<?> e);

	public void writeInt(int i);

	public void writeString(String str);

	public void writeBoolean(boolean bool);

	public void writeArray(byte[] data);

	public void writeFixedArray(byte[] data);

	public void writeWriterContent(IByteWriter writer);

	public void writeWriterWithChecksum(IByteWriter writer);

	public byte[] toByteArray();

	public void close();
	
	public static IByteWriter get(IImplementationFactory factory, String name){
		IByteWriter writer =  factory.provide(IByteWriter.class, new String[0], true, true, name);
		writer.setMemoryOutput();
		return writer;
	}

}