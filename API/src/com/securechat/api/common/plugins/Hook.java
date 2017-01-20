package com.securechat.api.common.plugins;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.securechat.api.common.Sides;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Hook {

	String name();

	Hooks hook();

	Sides side() default Sides.Both;

	String[] before() default {};

	String[] optBefore() default {};

	String[] after() default {};

	String[] optAfter() default {};
	
}
