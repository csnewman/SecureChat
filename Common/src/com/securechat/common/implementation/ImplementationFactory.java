package com.securechat.common.implementation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.securechat.common.plugins.Inject;
import com.securechat.common.plugins.Plugin;
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

	public void inject(Object obj) {
		Class<?> clazz = obj.getClass();

		String baseId = null;
		if (clazz.isAnnotationPresent(Plugin.class)) {
			baseId = clazz.getDeclaredAnnotation(Plugin.class).name();
		} else if (obj instanceof IImplementation) {
			baseId = ((IImplementation) obj).getImplName();
		}

		List<Field> fields = getAllFields(clazz, new ArrayList<Field>());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Inject.class)) {
				Inject annotation = field.getAnnotation(Inject.class);
				field.setAccessible(true);
				try {
					if (field.get(obj) == null) {
						Object value = provide((Class) field.getType(), annotation.providers(),
								annotation.allowDefault(), annotation.associate(), baseId);
						field.set(obj, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<Field> getAllFields(Class<?> clazz, List<Field> fields) {
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		if (clazz.getSuperclass() != null) {
			fields = getAllFields(clazz.getSuperclass(), fields);
		}

		return fields;
	}

	public <T extends IImplementation> Implementation<T> registerSingle(String name, Class<T> type,
			Supplier<? extends T> supplier) {
		Implementation<T> implementation = register(name, type, new SingleInstance<>(this, supplier));
		implementation.setInject(false);
		return implementation;
	}

	public <T extends IImplementation> Implementation<T> registerInstance(String name, Class<T> type, T inst) {
		inject(inst);
		Implementation<T> implementation = register(name, type, () -> inst);
		implementation.setInject(false);
		return implementation;
	}

	public <T extends IImplementation> Implementation<T> register(String name, Class<T> type,
			Supplier<? extends T> supplier) {
		Map<String, Implementation<? extends T>> map = getImplementations(type);
		Implementation<T> implementation = new Implementation<T>(name, type, supplier);
		map.put(name, implementation);
		return implementation;
	}

	public <T extends IImplementation> Map<String, Implementation<? extends T>> getImplementations(Class<T> type) {
		if (!implementations.containsKey(type)) {
			implementations.put(type, new HashMap<String, Implementation<T>>());
		}
		return implementations.get(type);
	}

	public <T extends IImplementation> boolean doesProviderExist(Class<T> type, String impName) {
		return getImplementations(type).containsKey(impName);
	}

	public <T extends IImplementation> T provide(Class<T> type, String[] providers, boolean allowDefault,
			boolean associate, String associateName) {
		PrimitiveProperty<String> associateProperty = new PrimitiveProperty<String>(
				associateName + "::" + type.getName(), null);

		if (propertyCollection.get(associateProperty) != null) {
			return provide(type, propertyCollection.get(associateProperty));
		}

		T value = null;
		String used = null;
		for (String provider : providers) {
			if (value == null && doesProviderExist(type, provider)) {
				value = provide(type, provider);
				used = provider;
			}
		}

		if (value == null && allowDefault) {
			PrimitiveProperty<String> property = new PrimitiveProperty<String>(type.getName(),
					defaults.get(type.getName()));
			used = propertyCollection.getPermissive(property);
			return provide(type, used);
		}

		if (value != null && associate) {
			propertyCollection.set(associateProperty, used);
		}

		return value;
	}

	public <T extends IImplementation> T provide(Class<T> type, String impName) {
		Implementation<? extends T> implementation = getImplementations(type).get(impName);
		T value = implementation.provide();
		if (implementation.shouldInject()) {
			inject(value);
		}
		return value;
	}

	public <T extends IImplementation> T provide(Class<T> type) {
		PrimitiveProperty<String> property = new PrimitiveProperty<String>(type.getName(),
				defaults.get(type.getName()));
		Implementation<? extends T> implementation = getImplementations(type)
				.get(propertyCollection.getPermissive(property));
		T value = implementation.provide();
		if (implementation.shouldInject()) {
			inject(value);
		}
		return value;
	}

	public <T extends IImplementation> void setFallbackDefault(Class<T> type, String defaultName) {
		defaults.put(type.getName(), defaultName);
		propertyCollection.getPermissive(new PrimitiveProperty<String>(type.getName(), defaultName));
	}
}
