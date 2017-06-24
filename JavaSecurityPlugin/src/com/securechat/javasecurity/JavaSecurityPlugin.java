package com.securechat.javasecurity;

import com.securechat.api.common.IContext;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.security.IHasher;
import com.securechat.api.common.security.IKeystore;
import com.securechat.api.common.security.IPasswordEncryption;

/**
 * An official plugin providing the reference implementations of the security
 * features based off the java security features.
 */
@Plugin(name = JavaSecurityPlugin.NAME, version = JavaSecurityPlugin.VERSION)
public class JavaSecurityPlugin {
	public static final String NAME = "official-java_security", VERSION = "1.0.0";

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		IImplementationFactory factory = context.getImplementationFactory();
		factory.register(PasswordEncryption.MARKER, IPasswordEncryption.class, PasswordEncryption::new);
		factory.register(RSAEncryption.MARKER, IAsymmetricKeyEncryption.class, RSAEncryption::new);
		factory.register(BasicKeystore.MARKER, IKeystore.class, BasicKeystore::new);
		factory.register(SHAHasher.MARKER, IHasher.class, SHAHasher::new);
	}

}
