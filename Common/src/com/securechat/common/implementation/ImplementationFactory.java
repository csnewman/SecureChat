package com.securechat.common.implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.securechat.common.properties.PrimitiveProperty;
import com.securechat.common.properties.PropertyCollection;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ImplementationFactory {
	private Map<Class, Map> implementations;
	private Map<String, String> defaults;
	private PropertyCollection propertyCollection;

	public ImplementationFactory(PropertyCollection propertyCollection) {
		implementations = new HashMap<Class, Map>();
		defaults = new HashMap<String, String>();
		this.propertyCollection = propertyCollection;
	}

	public <T extends IImplementation> void register(String name, Class<T> type, Supplier<? extends T> supplier) {
		Map<String, Implementation<? extends T>> map = getImplementations(type);
		map.put(name, new Implementation<T>(name, type, supplier));
	}

	public <T extends IImplementation> Map<String, Implementation<? extends T>> getImplementations(Class<T> type) {
		if (!implementations.containsKey(type)) {
			implementations.put(type, new HashMap<String, Implementation<T>>());
		}
		return implementations.get(type);
	}

	public <T extends IImplementation> T provide(Class<T> type, String impName) {
		return getImplementations(type).get(impName).provide();
	}

	public <T extends IImplementation> T provide(Class<T> type) {
		PrimitiveProperty<String> property = new PrimitiveProperty<String>(type.getName(),
				defaults.get(type.getName()));
		return getImplementations(type).get(propertyCollection.getPermissive(property)).provide();
	}

	public <T extends IImplementation> void setFallbackDefault(Class<T> type, String defaultName) {
		defaults.put(type.getName(), defaultName);
		propertyCollection.getPermissive(new PrimitiveProperty<String>(type.getName(), defaultName));
	}
}
