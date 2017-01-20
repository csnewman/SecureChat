package com.securechat.common.implementation;

import java.util.function.Supplier;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationInstance;

public class ImplementationInstance<T extends IImplementation> implements IImplementationInstance<T> {
	private String name;
	private Class<T> type;
	private Supplier<? extends T> supplier;
	private boolean inject;

	public ImplementationInstance(String name, Class<T> type, Supplier<? extends T> supplier, boolean inject) {
		this.name = name;
		this.type = type;
		this.supplier = supplier;
		this.inject = inject;
	}

	@Override
	public T provide() {
		return supplier.get();
	}

	@Override
	public void setInject(boolean inject) {
		this.inject = inject;
	}

	@Override
	public boolean shouldInject() {
		return inject;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

}
