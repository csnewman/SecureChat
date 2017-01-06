package com.securechat.plugins.rsakeystore;

import java.io.File;

import com.securechat.common.api.IKeystore;

public class RSAKeystore implements IKeystore {
	private File file;
	private boolean loaded;

	public RSAKeystore(File file) {
		this.file = file;
	}

	@Override
	public boolean generate(String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean load(String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public String getImplName() {
		return "official-rsakeystore";
	}

}
