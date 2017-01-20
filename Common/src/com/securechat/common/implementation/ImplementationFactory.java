package com.securechat.common.implementation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.IImplementationInstance;
import com.securechat.api.common.plugins.Inject;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.api.common.properties.PrimitiveProperty;
import com.securechat.api.common.properties.PropertyCollection;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ImplementationFactory implements IImplementationFactory {
	private ILogger log;
	private Map<Class, Map> implementations;
	private Map<Class, Object> instances;
	private Map<String, String> defaults;
	private PropertyCollection propertyCollection;

	public ImplementationFactory(ILogger log, PropertyCollection propertyCollection) {
		this.log = log;
		implementations = new HashMap<Class, Map>();
		defaults = new HashMap<String, String>();
		this.propertyCollection = propertyCollection;
		instances = new HashMap<Class, Object>();
	}

	@Override
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
			} else if (field.isAnnotationPresent(InjectInstance.class)) {
				log.debug("Found field " + field.getName() + " as " + field.getType());
				InjectInstance annotation = field.getAnnotation(InjectInstance.class);
				field.setAccessible(true);
				try {
					if (field.get(obj) == null) {
						Object value = get(field.getType(), annotation.provide());
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

	@Override
	public <T> T get(Class<T> type, boolean provide) {
		if (!instances.containsKey(type) && provide) {
			instances.put(type, provide((Class) type, new String[0], true, false, null));
		}
		return (T) instances.get(type);
	}

	@Override
	public void set(Class<?> type, Object instance) {
		instances.put(type, instance);
	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> registerInstance(String name, Class<T> type, T inst) {
		inject(inst);
		return register(name, type, () -> inst, false);
	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> register(String name, Class<T> type,
			Supplier<? extends T> supplier) {
		return register(name, type, supplier, true);
	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> register(String name, Class<T> type,
			Supplier<? extends T> supplier, boolean inject) {
		Map<String, IImplementationInstance<? extends T>> map = getImplementations(type);
		ImplementationInstance<T> implementation = new ImplementationInstance<T>(name, type, supplier, inject);
		map.put(name, implementation);
		return implementation;
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type) {
		return provide(type, new String[0], true, false, null);
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type, String impName) {
		return provide(type, new String[] { impName }, false, false, null);
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type, String[] providers, boolean allowDefault,
			boolean associate, String associateName) {
		log.debug("Providing " + type.getName());

		Map<String, IImplementationInstance<? extends T>> implementations = getImplementations(type);
		String provider = getProvider(type, providers, allowDefault, associate, associateName);

		log.debug("Implementations: " + implementations);
		log.debug("Provider: " + provider);

		if (!implementations.containsKey(provider)) {
			log.error("Implementation " + provider + " not found for " + type.getName() + "!");
			return null;
		}

		IImplementationInstance<? extends T> implementation = implementations.get(provider);
		T value = implementation.provide();
		if (implementation.shouldInject()) {
			inject(value);
		}
		return value;
	}

	@Override
	public <T extends IImplementation> String getProvider(Class<T> type, String[] providers, boolean allowDefault,
			boolean associate, String associateName) {
		log.debug("Finding provider for " + type.getName());

		PrimitiveProperty<String> associateProperty = null;
		if (associate) {
			associateProperty = new PrimitiveProperty<String>(associateName + "/" + type.getName(), null);
			String provider = propertyCollection.get(associateProperty);
			if (provider != null) {
				log.debug("Found associated provider " + provider);
				return provider;
			}
		}

		for (String provider : providers) {
			if (doesProviderExist(type, provider)) {
				log.debug("Found provider " + provider);
				if (associate)
					propertyCollection.set(associateProperty, provider);
				return provider;
			}
		}

		if (allowDefault) {
			String provider = getDefault(type);
			if (provider == null) {
				log.debug("Using default provider " + provider);
			} else {
				Map map = getImplementations(type);
				if (map.size() > 0) {
					provider = ((ImplementationInstance<? extends T>) map.values().iterator().next()).getName();
					log.debug("No default found, using first: " + provider);
				}
			}
			if (associate)
				propertyCollection.set(associateProperty, provider);
			return provider;
		}

		return null;
	}

	@Override
	public <T extends IImplementation> Map<String, IImplementationInstance<? extends T>> getImplementations(
			Class<T> type) {
		if (!implementations.containsKey(type)) {
			implementations.put(type, new HashMap<String, ImplementationInstance<T>>());
		}
		return implementations.get(type);
	}

	@Override
	public <T extends IImplementation> boolean doesProviderExist(Class<T> type, String impName) {
		return getImplementations(type).containsKey(impName);
	}

	@Override
	public <T extends IImplementation> void setFallbackDefaultIfNone(Class<T> type, String defaultName) {
		if (defaults.containsKey(type.getName()))
			return;
		setFallbackDefault(type, defaultName);
	}

	@Override
	public <T extends IImplementation> void setFallbackDefault(Class<T> type, String defaultName) {
		defaults.put(type.getName(), defaultName);
	}

	@Override
	public void flushDefaults() {
		for (Entry<String, String> entry : defaults.entrySet()) {
			propertyCollection.getPermissive(new PrimitiveProperty<String>(entry.getKey(), entry.getValue()));
		}
	}

	@Override
	public <T extends IImplementation> String getDefault(Class<T> type) {
		PrimitiveProperty<String> property = new PrimitiveProperty<String>(type.getName(),
				defaults.get(type.getName()));
		return propertyCollection.getPermissive(property);
	}
}
