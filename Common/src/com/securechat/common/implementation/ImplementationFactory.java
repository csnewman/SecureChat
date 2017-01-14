package com.securechat.common.implementation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.securechat.common.ILogger;
import com.securechat.common.plugins.Inject;
import com.securechat.common.plugins.Plugin;
import com.securechat.common.properties.PrimitiveProperty;
import com.securechat.common.properties.PropertyCollection;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ImplementationFactory {
	private ILogger log;
	private Map<Class, Map> implementations;
	private Map<String, String> defaults;
	private PropertyCollection propertyCollection;

	public ImplementationFactory(ILogger log, PropertyCollection propertyCollection) {
		this.log = log;
		implementations = new HashMap<Class, Map>();
		defaults = new HashMap<String, String>();
		this.propertyCollection = propertyCollection;
	}

	public void inject(Object obj) {
		Class<?> clazz = obj.getClass();

		String baseId = null;
		if (clazz.isAnnotationPresent(Plugin.class)) {
			baseId = clazz.getDeclaredAnnotation(Plugin.class).name();
			log.debug("Injecting into " + obj + " (plugin)");
		} else if (obj instanceof IImplementation) {
			baseId = ((IImplementation) obj).getImplName();
			log.debug("Injecting into " + obj + " (implementation)");
		} else {
			log.debug("Injecting into " + obj + " (unknown)");
		}

		List<Field> fields = getAllFields(clazz, new ArrayList<Field>());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Inject.class)) {
				log.debug("Found field " + field.getName() + " as " + field.getType());
				Inject annotation = field.getAnnotation(Inject.class);
				field.setAccessible(true);
				try {
					if (field.get(obj) == null) {
						Object value = provide((Class) field.getType(), annotation.providers(),
								annotation.allowDefault(), annotation.associate(), baseId);
						log.debug("Setting field to " + value);
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
		return register(name, type, new SingleInstance<>(this, supplier), false);
	}

	public <T extends IImplementation> Implementation<T> registerFixed(String owner, Class<T> type,
			Supplier<? extends T> supplier) {
		String id = "auto_" + type.getName() + "_supplier_" + owner;
		setFallbackDefaultIfNone(type, id);
		return register(id, type, supplier, false);
	}

	public <T extends IImplementation> Implementation<T> registerFixedInstance(String owner, Class<T> type, T inst) {
		String id = "auto_" + type.getName() + "_" + inst.getClass().getName() + "_" + owner;
		setFallbackDefaultIfNone(type, id);
		return registerInstance(id, type, inst);
	}

	public <T extends IImplementation> Implementation<T> registerInstance(String name, Class<T> type, T inst) {
		inject(inst);
		return register(name, type, () -> inst, false);
	}

	public <T extends IImplementation> Implementation<T> register(String name, Class<T> type,
			Supplier<? extends T> supplier) {
		return register(name, type, supplier, true);
	}

	public <T extends IImplementation> Implementation<T> register(String name, Class<T> type,
			Supplier<? extends T> supplier, boolean inject) {
		Map<String, Implementation<? extends T>> map = getImplementations(type);
		Implementation<T> implementation = new Implementation<T>(name, type, supplier, inject);
		map.put(name, implementation);
		return implementation;
	}

	public <T extends IImplementation> T provide(Class<T> type) {
		return provide(type, new String[0], true, false, null);
	}

	public <T extends IImplementation> T provide(Class<T> type, String impName) {
		return provide(type, new String[] { impName }, false, false, null);
	}

	public <T extends IImplementation> T provide(Class<T> type, String[] providers, boolean allowDefault,
			boolean associate, String associateName) {
		log.debug("Providing " + type.getName());

		Map<String, Implementation<? extends T>> implementations = getImplementations(type);
		String provider = getProvider(type, providers, allowDefault, associate, associateName);

		log.debug("Implementations: " + implementations);
		log.debug("Provider: " + provider);

		if (!implementations.containsKey(provider)) {
			log.error("Implementation " + provider + " not found for " + type.getName() + "!");
			return null;
		}

		Implementation<? extends T> implementation = implementations.get(provider);
		T value = implementation.provide();
		if (implementation.shouldInject()) {
			inject(value);
		}
		return value;
	}

	public <T extends IImplementation> String getProvider(Class<T> type, String[] providers, boolean allowDefault,
			boolean associate, String associateName) {
		log.debug("Finding provider for " + type.getName());

		PrimitiveProperty<String> associateProperty = null;
		if (associate) {
			associateProperty = new PrimitiveProperty<String>(associateName + "::" + type.getName(), null);
			String provider = propertyCollection.get(associateProperty);
			if (provider != null) {
				log.debug("Found associated provider " + provider);
				return provider;
			}
		}

		for (String provider : providers) {
			if (doesProviderExist(type, provider)) {
				log.debug("Found provider " + provider);
				propertyCollection.set(associateProperty, provider);
				return provider;
			}
		}

		if (allowDefault) {
			String provider = getDefault(type);
			log.debug("Using default provider " + provider);
			propertyCollection.set(associateProperty, provider);
			return provider;
		}

		return null;
	}

	public <T extends IImplementation> void setFallbackDefault(Class<T> type, String defaultName) {
		defaults.put(type.getName(), defaultName);
		propertyCollection.getPermissive(new PrimitiveProperty<String>(type.getName(), defaultName));
	}

	public <T extends IImplementation> void setFallbackDefaultIfNone(Class<T> type, String defaultName) {
		if (defaults.containsKey(type.getName()))
			return;
		defaults.put(type.getName(), defaultName);
		propertyCollection.getPermissive(new PrimitiveProperty<String>(type.getName(), defaultName));
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

	public <T extends IImplementation> String getDefault(Class<T> type) {
		PrimitiveProperty<String> property = new PrimitiveProperty<String>(type.getName(),
				defaults.get(type.getName()));
		return propertyCollection.getPermissive(property);
	}
}
