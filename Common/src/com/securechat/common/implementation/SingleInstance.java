package com.securechat.common.implementation;

import java.util.function.Supplier;

public class SingleInstance<T> implements Supplier<T> {
	private ImplementationFactory factory;
	private Supplier<T> provider;
	private T value;

	public SingleInstance(ImplementationFactory factory, Supplier<T> provider) {
		this.factory = factory;
		this.provider = provider;
	}

	@Override
	public T get() {
		if (value == null) {
			value = provider.get();
			factory.inject(value);
		}
		return value;
	}

}
