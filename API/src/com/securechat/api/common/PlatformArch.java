package com.securechat.api.common;

/**
 * Represents different architectures.
 */
public enum PlatformArch {
	X86_64("64"), X86_32("32");

	private String bitName;

	private PlatformArch(String bitName) {
		this.bitName = bitName;
	}

	public String getBitName() {
		return bitName;
	}

}
