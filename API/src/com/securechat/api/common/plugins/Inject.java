package com.securechat.api.common.plugins;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Inject {

	public String[] providers() default {};

	public boolean allowDefault() default true;

	public boolean associate() default false;
	
}
