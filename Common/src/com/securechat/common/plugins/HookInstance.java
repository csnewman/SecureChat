package com.securechat.common.plugins;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.securechat.api.common.Sides;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;

public class HookInstance {
	private PluginInstance plugin;
	private String name;
	private Hooks hook;
	private Sides side;
	private String[] before, after, optBefore, optAfter;
	private Method method;

	public HookInstance(PluginInstance plugin, Hook hook, Method method) {
		this.plugin = plugin;
		this.name = plugin.getName() + "/" + hook.name();
		this.hook = hook.hook();
		this.side = hook.side();
		this.before = hook.before();
		this.after = hook.after();
		this.optBefore = hook.optBefore();
		this.optAfter = hook.optAfter();
		this.method = method;
	}

	public void invoke(Object[] args) {
		try {
			method.invoke(plugin.getInstance(), args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public Hooks getHook() {
		return hook;
	}

	public Sides getSide() {
		return side;
	}

	public String[] getBefore() {
		return before;
	}

	public String[] getAfter() {
		return after;
	}

	public String[] getOptBefore() {
		return optBefore;
	}

	public String[] getOptAfter() {
		return optAfter;
	}
}
