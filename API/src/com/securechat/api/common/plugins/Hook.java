package com.securechat.api.common.plugins;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.securechat.api.common.Sides;

/**
 * Defines a hook on the given method. Only works for methods in the main plugin
 * class.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Hook {

	/**
	 * The (plugin) unique name of hook so that other hooks can depend on it.
	 * 
	 * @return the hooks name
	 */
	String name();

	/**
	 * The type of hook.
	 * 
	 * @return the type of hook
	 */
	Hooks hook();

	/**
	 * States whether the hook should be called server or client side.
	 * 
	 * @return the side to call the hook on
	 */
	Sides side() default Sides.Both;

	/**
	 * A list of hard dependents. This hook will always be called before these
	 * other hooks. Hooks are addressed in the format "{plugin name}/{hook
	 * name}"
	 * 
	 * @return the hooks to call after this
	 */
	String[] before() default {};

	/**
	 * A list of optional dependents. This hook will be called before these
	 * other hooks if they exist. Hooks are addressed in the format "{plugin
	 * name}/{hook name}"
	 * 
	 * @return the hooks to call after this
	 */
	String[] optBefore() default {};

	/**
	 * A list of hard dependencies This hook will always be called after these
	 * other hooks. Hooks are addressed in the format "{plugin name}/{hook
	 * name}"
	 * 
	 * @return the hooks to call first
	 */
	String[] after() default {};

	/**
	 * A list of optional dependencies. This hook will be called after these
	 * other hooks if they exist. Hooks are addressed in the format "{plugin
	 * name}/{hook name}"
	 * 
	 * @return the hooks to call first
	 */
	String[] optAfter() default {};

}
