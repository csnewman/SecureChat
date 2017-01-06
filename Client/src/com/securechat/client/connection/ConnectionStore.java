package com.securechat.client.connection;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.LinkedList;
import java.util.List;

import com.securechat.basicencryption.RSAEncryption;
import com.securechat.common.ByteReader;
import com.securechat.common.ByteWriter;
import com.securechat.common.security.ProtectedStore;

public class ConnectionStore extends ProtectedStore {
	private static final File connectionsFile = new File("connections.bin");
	private List<ConnectionInfo> infos;

	public ConnectionStore(KeyPair key) {
		super(connectionsFile, new RSAEncryption(key));
		infos = new LinkedList<ConnectionInfo>();
	}

	public void addConnection(ConnectionInfo info) {
		infos.add(info);
		save();
	}

	@Override
	protected void loadContent(ByteReader bodyReader) throws IOException {
		int size = bodyReader.readInt();
		infos.clear();
		for (int i = 0; i < size; i++) {
			infos.add(new ConnectionInfo(bodyReader));
		}
	}

	@Override
	protected void writeContent(ByteWriter writer) {
		writer.writeInt(infos.size());
		for (ConnectionInfo info : infos) {
			info.write(writer);
		}
	}

	public List<ConnectionInfo> getInfos() {
		return infos;
	}

}
