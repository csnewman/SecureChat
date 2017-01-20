package com.securechat.api.common.plugins;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.securechat.api.common.Sides;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Plugin {

	String name();
	
	String version();

	Sides side() default Sides.Both;
}
