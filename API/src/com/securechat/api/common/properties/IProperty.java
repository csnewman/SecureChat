package com.securechat.api.common.properties;

import org.json.JSONObject;

public interface IProperty<T> {

	public String getName();

	public void store(JSONObject obj, T value);

	public void storeDefault(JSONObject obj);

	public T load(JSONObject obj);

	public T getDefault();

}