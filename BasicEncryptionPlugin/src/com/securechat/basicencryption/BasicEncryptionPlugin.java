package com.securechat.basicencryption;

import com.securechat.common.IContext;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.Hook;
import com.securechat.common.plugins.Hooks;
import com.securechat.common.plugins.Plugin;
import com.securechat.common.security.IAsymmetricKeyEncryption;
import com.securechat.common.security.IPasswordEncryption;

@Plugin(name = "official-encryption", version = "1.0.0")
public class BasicEncryptionPlugin {

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		ImplementationFactory factory = context.getImplementationFactory();
		factory.register("official-password_encryption", IPasswordEncryption.class, PasswordEncryption::new);
		factory.register("official-rsa_encryption", IAsymmetricKeyEncryption.class, RSAEncryption::new);
	}

}
