package com.securechat.api.common.plugins;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a field that needs to be set during injection from the implementation
 * factory. If the field has a value, injection will skip this field.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Inject {

	/**
	 * A list of preferred implementations refereed by their id. Checked in
	 * order.
	 * 
	 * @return a list of implementations to look for first
	 */
	String[] providers() default {};

	/**
	 * Whether to use the default if no implementation is found from the
	 * providers.
	 * 
	 * @return whether to use the default
	 */
	boolean allowDefault() default true;

	/**
	 * Whether to associate the used implementation with this class/instance for
	 * future use.
	 * 
	 * @return whether to associate the implementation
	 */
	boolean associate() default false;

}
