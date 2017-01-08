package com.securechat.common.plugins;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
