package com.securechat.plugins.socketnetworking;

import com.securechat.api.client.network.IClientNetworkManager;
import com.securechat.api.common.IContext;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.api.server.network.IServerNetworkManager;
import com.securechat.plugins.socketnetworking.client.ClientNetworkManager;
import com.securechat.plugins.socketnetworking.server.ServerNetworkManager;

/**
 * An official plugin providing the reference implementations of socket based
 * networking.
 */
@Plugin(name = SocketNetworkingPlugin.NAME, version = SocketNetworkingPlugin.VERSION)
public class SocketNetworkingPlugin {
	public static final String NAME = "official-socket_networking", VERSION = "1.0.0";
	private IImplementationFactory factory;

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		factory = context.getImplementationFactory();
	}

	@Hook(name = "init-client", hook = Hooks.Init, after = NAME + "/init", side = Sides.Client)
	public void initClient(IContext context) {
		factory.register(ClientNetworkManager.MARKER, IClientNetworkManager.class, ClientNetworkManager::new);
		factory.setFallbackDefaultIfNone(IClientNetworkManager.class, ClientNetworkManager.MARKER);
	}

	@Hook(name = "init-server", hook = Hooks.Init, after = NAME + "/init", side = Sides.Server)
	public void initServer(IContext context) {
		factory.register(ServerNetworkManager.MARKER, IServerNetworkManager.class, ServerNetworkManager::new);
		factory.setFallbackDefaultIfNone(IServerNetworkManager.class, ServerNetworkManager.MARKER);
	}

}