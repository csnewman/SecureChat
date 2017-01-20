package com.securechat.api.common.implementation;

public interface IImplementationInstance<T extends IImplementation> {

	public void setInject(boolean inject);

	public boolean shouldInject();

	public String getName();

	public Class<T> getType();

	public T provide();

}
