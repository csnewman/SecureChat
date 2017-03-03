package com.securechat.api.common.properties;

import org.json.JSONObject;

/**
 * A property that can be contained in a collection.
 * 
 * @param <T> the type stored by this property
 */
public interface IProperty<T> {

	public String getName();

	public void store(JSONObject obj, T value);

	public void storeDefault(JSONObject obj);

	public T load(JSONObject obj);

	public T getDefault();

}