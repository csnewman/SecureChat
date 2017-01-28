package com.securechat.basicsecurity;

import com.securechat.api.common.IContext;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.security.IPasswordEncryption;

@Plugin(name = BasicSecurityPlugin.NAME, version = BasicSecurityPlugin.VERSION)
public class BasicSecurityPlugin {
	public static final String NAME = "official-security", VERSION = "1.0.0";

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		IImplementationFactory factory = context.getImplementationFactory();
		factory.register(PasswordEncryption.MARKER, IPasswordEncryption.class, PasswordEncryption::new);
		factory.register(RSAEncryption.MARKER, IAsymmetricKeyEncryption.class, RSAEncryption::new);
		factory.register(BasicKeystore.MARKER, IKeystore.class, BasicKeystore::new);

		factory.setFallbackDefaultIfNone(IPasswordEncryption.class, PasswordEncryption.MARKER);
		factory.setFallbackDefaultIfNone(IPasswordEncryption.class, RSAEncryption.MARKER);
		factory.setFallbackDefaultIfNone(IKeystore.class, BasicKeystore.MARKER);
	}

}
