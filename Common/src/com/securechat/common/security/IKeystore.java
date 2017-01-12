package com.securechat.common.security;

import com.securechat.common.implementation.IImplementation;

public interface IKeystore extends IImplementation {

	public boolean generate(char[] password);

	public boolean load(char[] password);

	public void addAsymmetricKey(String name, byte[] publicKey, byte[] privateKey);

	public void addAsymmetricKey(String name, IAsymmetricKeyEncryption encryption);

	public byte[] getAsymmetricPublicKey(String name);

	public byte[] getAsymmetricPrivateKey(String name);

	public void loadAsymmetricKey(String name, IAsymmetricKeyEncryption encryption);

	public boolean exists();

	public boolean isLoaded();

}
