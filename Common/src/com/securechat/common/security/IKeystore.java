package com.securechat.common.security;

import com.securechat.common.implementation.IImplementation;

public interface IKeystore extends IImplementation {

	public boolean generate(char[] password);

	public boolean load(char[] password);

	public boolean exists();

	public boolean isLoaded();

}
