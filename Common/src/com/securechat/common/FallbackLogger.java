package com.securechat.common;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.ImplementationMarker;

public class FallbackLogger implements ILogger {
	public static final ImplementationMarker MARKER = new ImplementationMarker("inbuilt", "n/a", "fallback_logger",
			"1.0.0");
	private Sides side;

	@Override
	public void init(IContext context) {
		side = context.getSide();
	}

	@Override
	public void debug(String message) {
		System.out.println("[" + side + "] [DEBUG] " + message);
	}

	@Override
	public void info(String message) {
		System.out.println("[" + side + "] [INFO ] " + message);
	}

	@Override
	public void warning(String message) {
		System.out.println("[" + side + "] [WARN ] " + message);
	}

	@Override
	public void error(String message) {
		System.out.println("[" + side + "] [ERROR] " + message);
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
