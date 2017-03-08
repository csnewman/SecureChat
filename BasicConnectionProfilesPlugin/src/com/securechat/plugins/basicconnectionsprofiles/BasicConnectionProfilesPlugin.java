package com.securechat.plugins.basicconnectionsprofiles;

import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.common.IContext;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.Plugin;

/**
 * An official plugin providing the reference implementations of the connection
 * store and profiles.
 */
@Plugin(name = BasicConnectionProfilesPlugin.NAME, version = BasicConnectionProfilesPlugin.VERSION)
public class BasicConnectionProfilesPlugin {
	public static final String NAME = "official-connection_profiles", VERSION = "1.0.0";
	private IImplementationFactory factory;

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		factory = context.getImplementationFactory();
		factory.register(BasicConnectionProfileProvider.MARKER, IConnectionProfileProvider.class,
				BasicConnectionProfileProvider::new);
		factory.setFallbackDefaultIfNone(IConnectionProfileProvider.class, BasicConnectionProfileProvider.MARKER);
	}

	@Hook(name = "init-client", hook = Hooks.Init, after = NAME + "/init", side = Sides.Client)
	public void initClient(IContext context) {
		factory.register(BasicConnectionStore.MARKER, IConnectionStore.class, BasicConnectionStore::new);
		factory.setFallbackDefaultIfNone(IConnectionStore.class, BasicConnectionStore.MARKER);
	}

}
