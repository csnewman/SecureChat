package com.securechat.api.common;

/**
 * Represents the different types of host programs.
 */
public enum Sides {
	Client, Server, Both;

	/**
	 * Checks whether the side allows the other side type or vice versa.
	 * 
	 * @param other
	 *            the other side to check against
	 * @return whether one contains the other
	 */
	public boolean allows(Sides other) {
		if (this == Sides.Client) {
			return other != Sides.Server;
		} else if (this == Sides.Server) {
			return other != Sides.Client;
		}
		return true;
	}

}
