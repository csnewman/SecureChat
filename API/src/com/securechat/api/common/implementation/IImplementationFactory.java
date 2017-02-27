package com.securechat.api.common.implementation;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Provides access to different implementations of API features without
 * requiring knowledge of the implementation.
 */
public interface IImplementationFactory {

	/**
	 * Injects into an annotated class with the implementations known by the
	 * factory.
	 * 
	 * @param obj
	 *            the object to inject into
	 */
	void inject(Object obj);

	/**
	 * Gets the instance of the given type. Creates a new instance if needed.
	 * 
	 * @param type
	 *            the type needed
	 * @param provide
	 *            whether an instance should be created if needed
	 * @return the instance
	 */
	<T> T get(Class<T> type, boolean provide);

	/**
	 * Sets the instance of a type to the given instance.
	 * 
	 * @param type
	 *            the type to set
	 * @param instance
	 *            the instance
	 */
	<T> void set(Class<T> type, T instance);

	/**
	 * Registers an implementation in the form of an instance.
	 * 
	 * @param marker
	 *            the marker for the implementation
	 * @param type
	 *            the type of the implementation
	 * @param inst
	 *            the instance
	 * @return a reference to the implementation
	 */
	<T extends IImplementation> IImplementationInstance<T> registerInstance(ImplementationMarker marker, Class<T> type,
			T inst);

	/**
	 * Registers an implementation in the form of a supplier with injection
	 * enabled.
	 * 
	 * @param marker
	 *            the marker for the implementation
	 * @param type
	 *            the type of the implementation
	 * @param supplier
	 *            the supplier
	 * @return a reference to the implementation
	 */
	<T extends IImplementation> IImplementationInstance<T> register(ImplementationMarker marker, Class<T> type,
			Supplier<? extends T> supplier);

	/**
	 * Registers an implementation in the form of a supplier.
	 * 
	 * @param marker
	 *            the marker for the implementation
	 * @param type
	 *            the type of the implementation
	 * @param supplier
	 *            the supplier
	 * @param inject
	 *            whether to inject into the value
	 * @return a reference to the implementation
	 */
	<T extends IImplementation> IImplementationInstance<T> register(ImplementationMarker marker, Class<T> type,
			Supplier<? extends T> supplier, boolean inject);

	/**
	 * Provides an instance of the type.
	 * 
	 * @param type
	 *            the needed type
	 * @return the implementation instance
	 */
	<T extends IImplementation> T provide(Class<T> type);

	/**
	 * Provides an instance of the type with the given marker.
	 * 
	 * @param type
	 *            the needed type
	 * @param marker
	 *            the marker to look for
	 * @return the implementation instance
	 */
	<T extends IImplementation> T provide(Class<T> type, ImplementationMarker marker);

	/**
	 * Provides an instance of the type from one of the given providers or from
	 * the default. This is then associated for future use.
	 * 
	 * @param type
	 *            the needed type
	 * @param providers
	 *            markers to look for first
	 * @param allowDefault
	 *            whether to fall back to the default
	 * @param associate
	 *            whether to associate the provider for future use
	 * @param associateName
	 *            the name to associate with
	 * @return the implementation instance
	 */
	<T extends IImplementation> T provide(Class<T> type, ImplementationMarker[] providers, boolean allowDefault,
			boolean associate, String associateName);

	/**
	 * Returns the marker of the implementation to use.
	 * 
	 * @param type
	 *            the needed type
	 * @param providers
	 *            markers to look for first
	 * @param allowDefault
	 *            whether to fall back to the default
	 * @param associate
	 *            whether to associate the provider for future use
	 * @param associateName
	 *            the name to associate with
	 * @return the implementation marker
	 */
	<T extends IImplementation> ImplementationMarker getProvider(Class<T> type, ImplementationMarker[] providers,
			boolean allowDefault, boolean associate, String associateName);

	/**
	 * Returns all implementations of a given type.
	 * 
	 * @param type
	 *            the needed type
	 * @return a map of all implementations
	 */
	<T extends IImplementation> Map<ImplementationMarker, IImplementationInstance<? extends T>> getImplementations(
			Class<T> type);

	/**
	 * Checks whether a provider exists with the given marker for the given
	 * type.
	 * 
	 * @param type
	 *            the target type
	 * @param marker
	 *            the marker to look for
	 * @return whether that marker exists for the needed type
	 */
	<T extends IImplementation> boolean doesProviderExist(Class<T> type, ImplementationMarker marker);

	/**
	 * Sets the default provider to fallback on if no default has been set.
	 * 
	 * @param type
	 *            the target type
	 * @param defaultMarker
	 *            the implementation marker
	 */
	<T extends IImplementation> void setFallbackDefaultIfNone(Class<T> type, ImplementationMarker defaultMarker);

	/**
	 * Sets the default provider to fallback on.
	 * 
	 * @param type
	 *            the target type
	 * @param defaultMarker
	 *            the implementation marker
	 */
	<T extends IImplementation> void setFallbackDefault(Class<T> type, ImplementationMarker defaultMarker);

	/**
	 * Gets the default implementation marker for the given type.
	 * 
	 * @param type
	 *            the needed type
	 * @return the default implementation marker
	 */
	<T extends IImplementation> ImplementationMarker getDefault(Class<T> type);

}
