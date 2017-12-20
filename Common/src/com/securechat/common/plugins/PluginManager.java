package com.securechat.common.plugins;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.IPluginInstance;
import com.securechat.api.common.plugins.IPluginManager;
import com.securechat.api.common.plugins.Plugin;

/**
 * The inbuilt implementation of the plugin manager.
 */
public class PluginManager implements IPluginManager {
	private IContext context;
	private ILogger logger;
	private List<IPluginInstance> plugins;
	private Map<String, HookInstance> hooks;
	private Map<Hooks, List<HookInstance>> hookCache;

	public PluginManager(IContext context) {
		this.context = context;
		logger = context.getLogger();
		plugins = new ArrayList<IPluginInstance>();
		hooks = new HashMap<String, HookInstance>();
		hookCache = new HashMap<Hooks, List<HookInstance>>();
	}

	@Override
	public void invokeHook(Hooks hook, Object... objects) {
		// Calls the hook on all hook instances
		for (HookInstance inst : hookCache.get(hook)) {
			inst.invoke(objects);
		}
	}

	@Override
	public void loadPlugins() {
		// Fetches all classes
		List<String> classes = context.getStorage().loadPlugins();

		for (String clazzName : classes) {
			// Attempts to load the class
			Class<?> clazz = safeLoad(clazzName);
			if (clazz == null) {
				continue;
			}

			// Checks if the class is a plugin
			if (!clazz.isAnnotationPresent(Plugin.class)) {
				continue;
			}

			Plugin pluginAnnotation = clazz.getDeclaredAnnotation(Plugin.class);

			// Creates a new instance
			PluginInstance instance = new PluginInstance(pluginAnnotation, clazz);
			plugins.add(instance);
			logger.info("Found plugin " + instance.getFullString());

			instance.createInstance();

			// Checks each method
			for (Method method : clazz.getDeclaredMethods()) {
				// Checks if the method is a hook
				if (!method.isAnnotationPresent(Hook.class)) {
					continue;
				}
				Hook hookAnnotation = method.getDeclaredAnnotation(Hook.class);
				// Ensures that the plugin isn't static
				if (Modifier.isStatic(method.getModifiers())) {
					logger.warning("Hooks can not be on static methods!");
					continue;
				}

				// Bypasses private member protection
				method.setAccessible(true);

				// Create a new hook instance
				HookInstance hook = new HookInstance(instance, hookAnnotation, method);

				// Ensures the hooks name is unique
				if (hooks.containsKey(hook.getName())) {
					throw new RuntimeException("Hook already exists!");
				}

				// Store the hook
				hooks.put(hook.getName(), hook);
				logger.debug("Found hook " + hook.getName());
			}
		}
	}

	@Override
	public void regenerateCache() {
		hookCache.clear();
		// Generate each hook
		for (Hooks hook : Hooks.values()) {
			List<HookInstance> insts = generateHook(hook);
			// Store the generated order
			hookCache.put(hook, insts);

			logger.debug("Plugin hook cache rebuilt for " + hook + "!");
			for (int i = 0; i < insts.size(); i++) {
				logger.debug(i + ": " + (insts.get(i) != null ? insts.get(i).getName() : "root"));
			}
		}

	}

	/**
	 * Resolves the appropriate calling order of hooks ensure dependencies are
	 * called in the right order.
	 * 
	 * @param hook
	 *            the hook to resolve
	 * @return the ordered list of hook instances to call
	 */
	private List<HookInstance> generateHook(Hooks hook) {
		Map<String, HookNode> nodeMap = new HashMap<String, HookNode>();

		for (HookInstance inst : hooks.values()) {
			// Checks that the hook is for this 'side'
			if (context.getSide().allows(inst.getSide()) && inst.getHook() == hook) {
				// Generates a hook node
				nodeMap.put(inst.getName(), new HookNode(inst));
			}
		}

		for (HookInstance inst : hooks.values()) {
			// Checks that the hook is for this 'side'
			if (!context.getSide().allows(inst.getSide()) || inst.getHook() != hook) {
				continue;
			}

			HookNode node = nodeMap.get(inst.getName());

			// Resolves all dependencies.
			for (String name : inst.getAfter()) {
				node.addDependency(nodeMap.get(name));
			}

			for (String name : inst.getOptAfter()) {
				if (nodeMap.containsKey(name)) {
					node.addDependency(nodeMap.get(name));
				}
			}

			for (String name : inst.getBefore()) {
				nodeMap.get(name).addDependency(node);
			}

			for (String name : inst.getOptBefore()) {
				if (nodeMap.containsKey(name)) {
					nodeMap.get(name).addDependency(node);
				}
			}
		}

		// Finds all nodes that are not used as dependencies.
		List<String> notLoaded = new ArrayList<String>();
		notLoaded.addAll(nodeMap.keySet());
		for (HookNode node : nodeMap.values()) {
			for (HookNode dep : node.getDependencies()) {
				notLoaded.remove(dep.getInstance().getName());
			}
		}

		// Make a root node with these base dependencies
		HookNode root = new HookNode(null);
		for (String name : notLoaded) {
			root.addDependency(nodeMap.get(name));
		}

		List<HookNode> resolved = new LinkedList<HookNode>();
		List<HookNode> unresolved = new ArrayList<HookNode>();

		// Resolves the dependency tree
		resolveDependencies(root, resolved, unresolved);

		// Removes the fake root node
		resolved.remove(root);

		List<HookInstance> result = new LinkedList<HookInstance>();

		// Converts the tree into a ordered list
		for (HookNode node : resolved) {
			result.add(node.getInstance());
		}

		return result;
	}

	/**
	 * Resolves a dependency tree of hook nodes.
	 * 
	 * @param node
	 *            the current node
	 * @param resolved
	 *            the resolved nodes
	 * @param unresolved
	 *            the unresolved nodes
	 */
	private void resolveDependencies(HookNode node, List<HookNode> resolved, List<HookNode> unresolved) {
		// Marks this node as unresolved
		unresolved.add(node);
		for (HookNode dep : node.getDependencies()) {
			// If this node hasn't been resolved, resolve it
			if (!resolved.contains(dep)) {
				// If we are currently trying to resolve it and we encounter it
				// again, we have a circular dependency loop
				if (unresolved.contains(dep))
					throw new RuntimeException(
							"Circular dependency detected! " + node.getName() + " to " + dep.getName());
				// Resolve its dependencies
				resolveDependencies(dep, resolved, unresolved);
			}
		}
		// Marks this node as resolved
		resolved.add(node);
		unresolved.remove(node);
	}

	/**
	 * Attempts to load a class by its name, safely failing.
	 * 
	 * @param name
	 *            the class to load
	 * @return the loaded class
	 */
	private Class<?> safeLoad(String name) {
		try {
			return Class.forName(name);
		} catch (Exception e) {
			return null;
		} catch (Error e) {
			return null;
		}
	}

	@Override
	public List<IPluginInstance> getPlugins() {
		return plugins;
	}

}
