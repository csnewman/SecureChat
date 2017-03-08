package com.securechat.api.common;

/**
 * Represents different OSs.
 */
public enum OsType {
	Windows("win"), OSX("osx"), Linux("linux"), Unknown("unknown");

	private String shortName;

	private OsType(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}

}
