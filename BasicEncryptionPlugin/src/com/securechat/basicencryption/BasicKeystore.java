package com.securechat.basicencryption;

import com.securechat.common.plugins.Inject;
import com.securechat.common.security.IKeystore;
import com.securechat.common.security.IPasswordEncryption;

public class BasicKeystore implements IKeystore{
	
	@Inject(allowDefault = true)
	private IPasswordEncryption passwordEncryption;

	@Override
	public boolean generate(char[] password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean load(char[] password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoaded() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getImplName() {
		return "official-basic_keystore";
	}

}
