package com.securechat.common.implementation;

import java.util.function.Supplier;

import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationInstance;
import com.securechat.api.common.implementation.ImplementationMarker;

/**
 * The inbuilt implementation of the implementation instance.
 */
public class ImplementationInstance<T extends IImplementation> implements IImplementationInstance<T> {
	private ImplementationMarker marker;
	private Class<T> type;
	private Supplier<? extends T> supplier;
	private boolean inject;

	public ImplementationInstance(ImplementationMarker marker, Class<T> type, Supplier<? extends T> supplier,
			boolean inject) {
		this.marker = marker;
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
	public ImplementationMarker getMarker() {
		return marker;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

}
