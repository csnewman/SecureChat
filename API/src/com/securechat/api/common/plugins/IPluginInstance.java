package com.securechat.api.common.plugins;

import com.securechat.api.common.Sides;

public interface IPluginInstance {

	String getName();

	String getVersion();

	Sides getSide();

	Object getInstance();

}
