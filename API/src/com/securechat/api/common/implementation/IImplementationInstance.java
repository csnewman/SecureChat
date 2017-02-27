package com.securechat.api.common.implementation;

/**
 * Represents an available implementation.
 * 
 * @param <T>
 *            the type the implementation provides
 */
public interface IImplementationInstance<T extends IImplementation> {

	/**
	 * Gets the implementation marker for this instance.
	 * 
	 * @return the implementation marker
	 */
	ImplementationMarker getMarker();

	/**
	 * Gets the type that this implementation instance provides.
	 * 
	 * @return the type of the implementation
	 */
	Class<T> getType();

	/**
	 * Provides a new instance of this implementation.
	 * 
	 * @return the new instance
	 */
	T provide();

	/**
	 * Sets whether the new instance should be injected into by the
	 * implementation factory after creation.
	 * 
	 * @param inject
	 *            whether to inject
	 */
	void setInject(boolean inject);

	/**
	 * Returns whether the new instance should be injected into by the
	 * implementation factory after creation.
	 * 
	 * @return whether to inject
	 */
	boolean shouldInject();

}
