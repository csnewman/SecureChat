package com.securechat.common;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;

public class FallbackLogger implements ILogger {
	private Sides side;
	
	@Override
	public void init(IContext context) {
		side = context.getSide();
	}
	
	@Override
	public void debug(String message) {
		System.out.println("["+side+"] [DEBUG] "+message);
	}
	
	@Override
	public void info(String message) {
		System.out.println("["+side+"] [INFO ] "+message);
	}

	@Override
	public void warning(String message) {
		System.out.println("["+side+"] [WARN ] "+message);
	}

	@Override
	public void error(String message) {
		System.out.println("["+side+"] [ERROR] "+message);
	}
	
	@Override
	public String getImplName() {
		return "official-fallback_logger";
	}

}
