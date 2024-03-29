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
import com.securechat.api.common.properties.CollectionProperty;
import com.securechat.api.common.properties.PropertyCollection;

/**
 * The inbuilt implementation of the implementation factory.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ImplementationFactory implements IImplementationFactory {
	private ILogger log;
	private Map<Class, Map<String, IImplementationInstance>> implementations;
	private Map<Class, Object> instances;
	private Map<String, ImplementationMarker> defaults;
	private PropertyCollection baseCollection;

	public ImplementationFactory(ILogger log, PropertyCollection baseCollection) {
		this.log = log;
		implementations = new HashMap<Class, Map<String, IImplementationInstance>>();
		defaults = new HashMap<String, ImplementationMarker>();
		this.baseCollection = baseCollection;
		instances = new HashMap<Class, Object>();
	}

	@Override
	public void inject(Object obj) {
		Class<?> clazz = obj.getClass();

		log.debug("Injecting into " + obj);

		// Searches each field in the class
		List<Field> fields = getAllFields(clazz);
		for (Field field : fields) {
			// Checks if the field has an Inject annotation
			if (field.isAnnotationPresent(Inject.class)) {
				log.debug("Found field " + field.getName() + " as " + field.getType());
				Inject annotation = field.getAnnotation(Inject.class);
				field.setAccessible(true);

				try {
					// Checks whether there is a value or not for the field
					if (field.get(obj) == null) {
						// Generates a new instance
						Object value = provide((Class) field.getType(),
								ImplementationMarker.convert(annotation.providers()), annotation.allowDefault());

						// Sets the fields value
						log.debug("Setting field to " + value);
						field.set(obj, value);
					}
				} catch (Exception e) {
					// Ignore errors when injecting into field
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
		// Adds all fields in this class
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		// Adds all fields in parent classes
		if (clazz.getSuperclass() != null) {
			fields.addAll(getAllFields(clazz.getSuperclass()));
		}

		return fields;
	}

	@Override
	public <T> T get(Class<T> type, boolean provide) {
		// Checks if an instance should be generated
		if (!instances.containsKey(type) && provide) {
			// Generates a new instance and stores it
			instances.put(type, provide((Class) type));
		}
		return (T) instances.get(type);
	}

	@Override
	public <T> void set(Class<T> type, T instance) {
		// Stores the instance for that type
		instances.put(type, instance);
	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> registerInstance(ImplementationMarker marker,
			Class<T> type, T inst) {
		// Injects into the instance
		inject(inst);
		// Registers the instance
		return register(marker, type, () -> inst, false);

	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> register(ImplementationMarker marker, Class<T> type,
			Supplier<? extends T> supplier) {
		// Registers the instance, marked for injection
		return register(marker, type, supplier, true);
	}

	@Override
	public <T extends IImplementation> ImplementationInstance<T> register(ImplementationMarker marker, Class<T> type,
			Supplier<? extends T> supplier, boolean inject) {
		// Get the map of implementations for that type
		Map<ImplementationMarker, IImplementationInstance<? extends T>> map = getImplementations(type);
		// Creates a new implementation instance
		ImplementationInstance<T> implementation = new ImplementationInstance<T>(marker, type, supplier, inject);
		// Adds the implementation to the map
		map.put(marker, implementation);
		return implementation;
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type) {
		// Creates an instance of the provided type
		return provide(type, new ImplementationMarker[0], true);
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type, ImplementationMarker marker) {
		// Creates an instance of the provided type, using the specific implementation
		return provide(type, new ImplementationMarker[] { marker }, false);
	}

	@Override
	public <T extends IImplementation> T provide(Class<T> type, ImplementationMarker[] providers,
			boolean allowDefault) {
		log.debug("Providing " + type.getName());

		// Gets the implementation
		Map<ImplementationMarker, IImplementationInstance<? extends T>> implementations = getImplementations(type);

		// Gets the provider of the type
		ImplementationMarker provider = getProvider(type, providers, allowDefault);

		log.debug("Implementations: " + implementations);
		log.debug("Provider: " + provider);

		// Ensure it exists
		if (!implementations.containsKey(provider)) {
			log.error("Implementation " + provider + " not found for " + type.getName() + "!");
			return null;
		}

		// Gets the implementation
		IImplementationInstance<? extends T> implementation = implementations.get(provider);

		// Creates a new instance
		T value = implementation.provide();

		// Injects if needed
		if (implementation.shouldInject()) {
			inject(value);
		}
		return value;
	}

	@Override
	public <T extends IImplementation> ImplementationMarker getProvider(Class<T> type, ImplementationMarker[] providers,
			boolean allowDefault) {
		log.debug("Finding provider for " + type.getName());

		if (providers != null) {
			for (ImplementationMarker provider : providers) {
				// Checks if an association exists
				if (doesProviderExist(type, provider)) {
					log.debug("Found provider " + provider);
					return provider;
				}
			}
		}

		if (allowDefault) {
			// Falls back to default
			ImplementationMarker provider = getDefault(type);
			if (provider != null) {
				log.debug("Using default provider " + provider);
				return provider;
			} else {
				Map map = getImplementations(type);
				if (map.size() > 0) {
					provider = ((ImplementationInstance<? extends T>) map.values().iterator().next()).getMarker();
					log.debug("No default found, using first: " + provider);
					return provider;
				}
			}
		}

		log.warning("Failed to find a provider for " + type);
		return null;
	}

	@Override
	public <T extends IImplementation> Map<ImplementationMarker, IImplementationInstance<? extends T>> getImplementations(
			Class<T> type) {
		// Checks if a map exists
		if (!implementations.containsKey(type)) {
			// Create a new map
			implementations.put(type, (Map) new HashMap<String, ImplementationInstance<T>>());
		}
		return (Map) implementations.get(type);
	}

	@Override
	public Map<Class, Map<String, IImplementationInstance>> getAllImplementations() {
		return implementations;
	}

	@Override
	public <T extends IImplementation> boolean doesProviderExist(Class<T> type, ImplementationMarker marker) {
		return getImplementations(type).containsKey(marker);
	}

	@Override
	public <T extends IImplementation> void setDefault(Class<T> type, ImplementationMarker marker) {
		// Sets the default marker
		defaults.put(type.getName(), marker);
		// Creates a config property to store default
		CollectionProperty property = new CollectionProperty(type.getName());
		log.debug("Setting default for " + type.getName() + " to " + defaults.get(type.getName()));
		// Stores the property
		baseCollection.set(property, defaults.get(type.getName()).toJSON());
	}

	@Override
	public <T extends IImplementation> ImplementationMarker getDefault(Class<T> type) {
		CollectionProperty property = new CollectionProperty(type.getName());
		// Check if no property exists
		if (!baseCollection.exists(property)) {
			Map map = getImplementations(type);
			if (map.size() > 0) {
				// Selects the first implementation
				ImplementationMarker marker = ((ImplementationInstance<? extends T>) map.values().iterator().next())
						.getMarker();
				// Sets it as the default
				setDefault(type, marker);
				return marker;
			}

			return null;
		}
		// Loads the property
		return ImplementationMarker.loadMarker(baseCollection.get(property));
	}

}
