package com.securechat.api.common.plugins;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.securechat.api.common.Sides;

/**
 * Defines a class as plugin that will be auto detected and loaded.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Plugin {

	/**
	 * The unique name of this plugin.
	 * 
	 * @return the plugins name
	 */
	String name();

	/**
	 * The version of this plugin.
	 * 
	 * @return the plugins version
	 */
	String version();

	/**
	 * States whether the hook should be loaded server or client side.
	 * 
	 * @return
	 */
	Sides side() default Sides.Both;
}
