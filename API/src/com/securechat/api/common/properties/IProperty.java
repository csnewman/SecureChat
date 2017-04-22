package com.securechat.api.common.properties;

import org.json.JSONObject;

/**
 * A property that can be contained in a collection.
 * 
 * @param <T>
 *            the type stored by this property
 */
public interface IProperty<T> {

	String getName();

	void store(JSONObject obj, T value);

	void storeDefault(JSONObject obj);

	T load(JSONObject obj);

	T getDefault();

}