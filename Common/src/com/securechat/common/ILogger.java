package com.securechat.common;

import com.securechat.common.implementation.IImplementation;

public interface ILogger extends IImplementation {
	
	public void init(IContext context);
	
	public void debug(String message);
	
	public void info(String message);

	public void warning(String message);

	public void error(String message);
}
