package com.securechat.common.plugins;

public enum Sides {
	Client, Server, Both;

	public boolean allows(Sides other) {
		if (this == Sides.Client) {
			return other != Sides.Server;
		} else if (this == Sides.Server) {
			return other != Sides.Client;
		}
		return true;
	}

}
