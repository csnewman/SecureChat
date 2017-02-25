package com.securechat.api.common;

import com.securechat.api.common.implementation.IImplementation;

public interface ILogger extends IImplementation {
	
	public void init(IContext context, boolean debug);
	
	public void debug(String message);
	
	public void info(String message);

	public void warning(String message);

	public void error(String message);
}
