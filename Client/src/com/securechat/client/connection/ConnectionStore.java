package com.securechat.client.connection;

import java.io.File;
import java.security.KeyPair;
import java.util.LinkedList;
import java.util.List;

import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.security.ProtectedStore;
import com.securechat.common.security.RSAEncryption;

public class ConnectionStore extends ProtectedStore {
	private static final File connectionsFile = new File("connections.bin");
	private List<ConnectionInfo> infos;
	
	public ConnectionStore(KeyPair key) {
		super(connectionsFile, new RSAEncryption(key));
		infos = new LinkedList<ConnectionInfo>();
	}

	@Override
	protected void loadContent(ByteReader bodyReader) {
		int size = bodyReader.readInt();
		infos.clear();
		for(int i = 0; i < size; i++){
			ConnectionInfo info = new ConnectionInfo();
			info.load(bodyReader);
			infos.add(info);
		}
	}

	@Override
	protected void writeContent(ByteWriter writer) {
		writer.writeInt(infos.size());
		for(ConnectionInfo info : infos){
			info.save(writer);
		}
	}
	
	public List<ConnectionInfo> getInfos() {
		return infos;
	}
	
	
	

}
