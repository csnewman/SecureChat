package com.securechat.api.common;

import com.securechat.api.common.implementation.IImplementation;

/**
 * Outputs debug information
 */
public interface ILogger extends IImplementation {

	/**
	 * Configures the logger.
	 * 
	 * @param context
	 *            the context thats using the logger
	 * @param debug
	 *            whether debug information should be enabled
	 */
	void init(IContext context, boolean debug);

	/**
	 * Outputs the message as debug information.
	 * 
	 * @param message
	 *            the message to be shown
	 */
	void debug(String message);

	/**
	 * Outputs the message as info information.
	 * 
	 * @param message
	 *            the message to be shown
	 */
	void info(String message);

	/**
	 * Outputs the message as warning information.
	 * 
	 * @param message
	 *            the message to be shown
	 */
	void warning(String message);

	/**
	 * Outputs the message as error information.
	 * 
	 * @param message
	 *            the message to be shown
	 */
	void error(String message);

}
