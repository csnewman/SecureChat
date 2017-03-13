package com.securechat.plugins.defaultmanagers;

import com.securechat.api.client.IClientManager;
import com.securechat.api.client.chat.IClientChatManager;
import com.securechat.api.common.IContext;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.api.server.IServerChatManager;
import com.securechat.api.server.IServerManager;
import com.securechat.api.server.users.IUserManager;
import com.securechat.plugins.defaultmanagers.client.DefaultClientChatManager;
import com.securechat.plugins.defaultmanagers.client.DefaultClientManager;
import com.securechat.plugins.defaultmanagers.server.DefaultServerChatManager;
import com.securechat.plugins.defaultmanagers.server.DefaultServerManager;
import com.securechat.plugins.defaultmanagers.server.DefaultUserManager;

/**
 * An official plugin providing the reference implementations of the managers.
 */
@Plugin(name = DefaultManagersPlugin.NAME, version = DefaultManagersPlugin.VERSION)
public class DefaultManagersPlugin {
	public static final String NAME = "official-default_managers", VERSION = "1.0.0";
	private IImplementationFactory factory;

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		factory = context.getImplementationFactory();
	}

	@Hook(name = "init-client", hook = Hooks.Init, after = NAME + "/init", side = Sides.Client)
	public void initClient(IContext context) {
		factory.register(DefaultClientManager.MARKER, IClientManager.class, DefaultClientManager::new);
		factory.register(DefaultClientChatManager.MARKER, IClientChatManager.class, DefaultClientChatManager::new);
		factory.setFallbackDefaultIfNone(IClientManager.class, DefaultClientManager.MARKER);
		factory.setFallbackDefaultIfNone(IClientChatManager.class, DefaultClientChatManager.MARKER);
	}

	@Hook(name = "init-server", hook = Hooks.Init, after = NAME + "/init", side = Sides.Server)
	public void initServer(IContext context) {
		factory.register(DefaultServerManager.MARKER, IServerManager.class, DefaultServerManager::new);
		factory.register(DefaultServerChatManager.MARKER, IServerChatManager.class, DefaultServerChatManager::new);
		factory.register(DefaultUserManager.MARKER, IUserManager.class, DefaultUserManager::new);
		factory.setFallbackDefaultIfNone(IServerManager.class, DefaultServerManager.MARKER);
		factory.setFallbackDefaultIfNone(IServerChatManager.class, DefaultServerChatManager.MARKER);
		factory.setFallbackDefaultIfNone(IUserManager.class, DefaultUserManager.MARKER);
	}

}