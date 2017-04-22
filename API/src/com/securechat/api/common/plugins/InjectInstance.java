package com.securechat.api.common.plugins;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a field that needs to be set during injection from the implementation
 * factory with the instance of this type. If the field has a value, injection
 * will skip this field.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface InjectInstance {

	/**
	 * Whether to create and set a new instance if no instance exists.
	 * 
	 * @return whether to create a new instance if needed.
	 */
	boolean provide() default false;

}
