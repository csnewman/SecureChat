package com.securechat.common.implementation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.securechat.api.common.ILogger;
import com.securechat.api.common.implementation.IImplementation;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.IImplementationInstance;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.Inject;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PropertyCollection;

/**
 * The inbuilt implementation of the implementation factory.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ImplementationFactory implements IImplementationFactory {
	private ILogger log;
	private Map<Class, Map> implementations;
	private Map<Class, Object> instances;
	private Map<String, ImplementationMarker> defaults;
	private PropertyCollection baseCollection, defaultsCollection;

	public ImplementationFactory(ILogger log, PropertyCollection baseCollection) {
		this.log = log;
		implementations = new HashMap<Class, Map>();
		defaults = new HashMap<String, ImplementationMarker>();
		this.baseCollection = baseCollection;
		defaultsCollection = baseCollection != null ? baseCollection.getPermissive(DEFAULTS_PROPERTY) : null;
		instances = new HashMap<Class, Object>();
	}

	@Override
	public void inject(Object obj) {
		Class<?> clazz = obj.getClass();

		// Tries to find an id for this object
		String baseId = null;
		if (clazz.isAnnotationPresent(Plugin.class)) {
			baseId = clazz.getDeclaredAnnotation(Plugin.class).name();
			log.debug("Injecting into " + obj + " (plugin)");
		} else if (obj instanceof IImplementation) {
			baseId = ((IImplementation) obj).getMarker().getId();
			log.debug("Injecting into " + obj + " (implementation)");
		} else {
			log.debug("Injecting into " + obj + " (unknown)");
		}

		// Searches each field in the class
		List<Field> fields = getAllFields(clazz);
		for (Field field : fields) {
			// Checks if the field has an Inject annotation
			if (field.isAnnotationPresent(Inject.class)) {
				log.debug("Found field " + field.getName() + " as " + field.getType());
				Inject annotation = field.getAnnotation(Inject.class);
				field.setAccessible(true);

				// Ensures that if the field is set to associate that an id has
				// been found
				if (annotation.associate() && baseId == null) {
					log.error("Associating usage to an unknown object");
				}

				try {
					// Checks whether there is a value or not for the field
					if (field.get(obj) == null) {
						// Generates a new instance
						Object value = provide((Class) field.getType(),
								ImplementationMarker.convert(annotation.providers()), annotation.allowDefault(),
								annotation.associate(), baseId);

						// Sets the fields value
						log.debug("Setting field to " + value);
						field.set(obj, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Checks for an InjectInstance
			} else if (field.isAnnotationPresent(InjectInstance.class)) {
				log.debug("Found field " + field.getName() + " as " + field.getType());
				InjectInstance annotation = field.getAnnotation(InjectInstance.class);
				field.setAccessible(true);
				try {
					// Checks whether there is a value or not for the field
					if (field.get(obj) == null) {
						// Gets the instance
						Object value = get(field.getType(), annotation.provide());

						// Sets the fields value
						log.debug("Setting field to " + value);
						field.set(obj, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Recursively looks for all fields in a class and its parents.
	 * 
	 * @param clazz
	 *            the class to search through
	 * @return the fields found
	 */
	private List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		if (clazz.getSuperclass() != null) {
			fields.addAll(getAllFields(clazz.getSuperclass()));
		}

		return fields;
	}

	@Override
	public <T> T get(Class<T> type, boolean provide) {
		// Checks if an instance should be generated
		if (!instances.containsKey(type) && provide) {
			instances.put(type, provide((Class) type, new ImplementationMarker[0], true, false, null));
		}
		return (T) instances.get(type);
	}

	@Override
	public <T> void set(Class<T> type, T instance) {
		instances.put(type, instance);
	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> registerInstance(ImplementationMarker marker,
			Class<T> type, T inst) {
		inject(inst);
		return register(marker, type, () -> inst, false);

	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> register(ImplementationMarker marker, Class<T> type,
			Supplier<? extends T> supplier) {
		return register(marker, type, supplier, true);
	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> register(ImplementationMarker marker, Class<T> type,
			Supplier<? extends T> supplier, boolean inject) {
		Map<ImplementationMarker, IImplementationInstance<? extends T>> map = getImplementations(type);
		ImplementationInstance<T> implementation = new ImplementationInstance<T>(marker, type, supplier, inject);
		map.put(marker, implementation);
		return implementation;
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type) {
		return provide(type, new ImplementationMarker[0], true, false, null);
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type, ImplementationMarker marker) {
		return provide(type, new ImplementationMarker[] { marker }, false, false, null);
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type, ImplementationMarker[] providers, boolean allowDefault,
			boolean associate, String associateName) {
		log.debug("Providing " + type.getName());

		// Gets the implementation
		Map<ImplementationMarker, IImplementationInstance<? extends T>> implementations = getImplementations(type);
		ImplementationMarker provider = getProvider(type, providers, allowDefault, associate, associateName);

		log.debug("Implementations: " + implementations);
		log.debug("Provider: " + provider);

		// Ensure it exists
		if (!implementations.containsKey(provider)) {
			log.error("Implementation " + provider + " not found for " + type.getName() + "!");
			return null;
		}

		// Configures the implementation
		IImplementationInstance<? extends T> implementation = implementations.get(provider);
		T value = implementation.provide();
		if (implementation.shouldInject()) {
			inject(value);
		}
		return value;
	}

	@Override
	public <T extends IImplementation> ImplementationMarker getProvider(Class<T> type, ImplementationMarker[] providers,
			boolean allowDefault, boolean associate, String associateName) {
		log.debug("Finding provider for " + type.getName());

		PropertyCollection collection = null;
		CollectionProperty property = null;
		if (associate) {
			if (associateName == null) {
				throw new RuntimeException("No associate name given!");
			}

			collection = baseCollection.getPermissive(new CollectionProperty(associateName));
			property = new CollectionProperty(type.getName());

			// Checks for an already associated provider
			ImplementationMarker marker = ImplementationMarker.loadMarker(collection.get(property));
			if (marker != null) {
				log.debug("Found associated provider " + marker);
				return marker;
			}
		}

		if (providers != null) {
			for (ImplementationMarker provider : providers) {
				// Checks if an association exists
				if (doesProviderExist(type, provider)) {
					log.debug("Found provider " + provider);
					// Stores the association
					if (associate)
						collection.set(property, provider.toJSON());
					return provider;
				}
			}
		}

		if (allowDefault) {
			// Falls back to default
			ImplementationMarker provider = getDefault(type);
			if (provider != null) {
				log.debug("Using default provider " + provider);
			} else {
				Map map = getImplementations(type);
				if (map.size() > 0) {
					provider = ((ImplementationInstance<? extends T>) map.values().iterator().next()).getMarker();
					log.debug("No default found, using first: " + provider);
				}
			}
			if (provider != null) {
				// Stores the association
				if (associate)
					collection.set(property, provider.toJSON());
				return provider;
			}
		}

		log.warning("Failed to find a provider for " + type);
		return null;
	}

	@Override
	public <T extends IImplementation> Map<ImplementationMarker, IImplementationInstance<? extends T>> getImplementations(
			Class<T> type) {
		if (!implementations.containsKey(type)) {
			implementations.put(type, new HashMap<String, ImplementationInstance<T>>());
		}
		return implementations.get(type);
	}

	@Override
	public <T extends IImplementation> boolean doesProviderExist(Class<T> type, ImplementationMarker marker) {
		return getImplementations(type).containsKey(marker);
	}

	@Override
	public <T extends IImplementation> void setFallbackDefaultIfNone(Class<T> type, ImplementationMarker marker) {
		if (defaults.containsKey(type.getName()))
			return;
		setFallbackDefault(type, marker);
	}

	@Override
	public <T extends IImplementation> void setFallbackDefault(Class<T> type, ImplementationMarker marker) {
		defaults.put(type.getName(), marker);
	}

	@Override
	public <T extends IImplementation> ImplementationMarker getDefault(Class<T> type) {
		CollectionProperty property = new CollectionProperty(type.getName());
		if (!defaultsCollection.exists(property)) {
			if (!defaults.containsKey(type.getName())) {
				return null;
			}
			log.debug("Setting default for " + type.getName() + " to " + defaults.get(type.getName()));
			defaultsCollection.set(property, defaults.get(type.getName()).toJSON());
		}
		return ImplementationMarker.loadMarker(defaultsCollection.get(property));
	}

	public static final CollectionProperty DEFAULTS_PROPERTY;
	static {
		DEFAULTS_PROPERTY = new CollectionProperty("defaults");
	}
}
