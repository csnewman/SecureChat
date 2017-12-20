package com.securechat.common;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.ImplementationMarker;

/**
 * A reference logger, directly outputting to the console.
 */
public class ConsoleLogger implements ILogger {
	private Sides side;
	private boolean showDebug;

	@Override
	public void init(IContext context, boolean showDebug) {
		side = context.getSide();
		this.showDebug = showDebug;
	}

	@Override
	public void debug(String message) {
		// Only print debug messages if debug is enabled
		if (showDebug)
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

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker("inbuilt", "n/a", "fallback_logger", "1.0.0");
	}

}
