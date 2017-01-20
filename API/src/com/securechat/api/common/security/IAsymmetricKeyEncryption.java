package com.securechat.api.common.security;

public interface IAsymmetricKeyEncryption extends IEncryption {

	public void generate();

	public void load(byte[] publicKey, byte[] privateKey);

	public byte[] getPublickey();

	public byte[] getPrivatekey();

}
