package com.securechat.common.api;

public interface IKeystore extends IImplementation {

	public boolean generate(String password);

	public boolean load(String password);

	public boolean exists();

	public boolean isLoaded();

}
