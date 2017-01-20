package com.securechat.basicsecurity;

import com.securechat.api.common.IContext;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.security.IPasswordEncryption;

@Plugin(name = "official-security", version = "1.0.0")
public class BasicSecurityPlugin {

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		IImplementationFactory factory = context.getImplementationFactory();
		factory.register("official-password_encryption", IPasswordEncryption.class, PasswordEncryption::new);
		factory.register("official-rsa_encryption", IAsymmetricKeyEncryption.class, RSAEncryption::new);
		factory.register("official-basic_keystore", IKeystore.class, BasicKeystore::new);
		
		factory.setFallbackDefaultIfNone(IPasswordEncryption.class, "official-password_encryption");
		factory.setFallbackDefaultIfNone(IPasswordEncryption.class, "official-rsa_encryption");
		factory.setFallbackDefaultIfNone(IKeystore.class, "official-basic_keystore");
	}

}
