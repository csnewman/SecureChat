package com.securechat.common.security;

import java.io.File;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;

public class ProtectedDataStore extends ProtectedStore{
	private byte[] content;

	public ProtectedDataStore(File file, IEncryption encryptionMethod) {
		super(file, encryptionMethod);
	}

	@Override
	protected void loadContent(ByteReader bodyReader) {
		content = bodyReader.readArray();
	}

	@Override
	protected void writeContent(ByteWriter writer) {
		writer.writeArray(content);
	}
	
	public byte[] getContent(){
		return content;
	}
	
	public ByteReader getReader(){
		return new ByteReader(content);
	}
	
	public void setContent(byte[] content){
		this.content = content;
	}
	
	public void setContent(ByteWriter writer){
		content = writer.toByteArray();
		writer.close();
	}
	
	
	

}
