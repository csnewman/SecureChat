package com.securechat.common.implementation;

import java.util.function.Supplier;

public class Implementation<T extends IImplementation> {
	private String name;
	private Class<T> type;
	private Supplier<? extends T> supplier;

	public Implementation(String name, Class<T> type, Supplier<? extends T> supplier) {
		this.name = name;
		this.type = type;
		this.supplier = supplier;
	}

	public String getName() {
		return name;
	}

	public Class<T> getType() {
		return type;
	}

	public T provide() {
		return supplier.get();
	}

}
