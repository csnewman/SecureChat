package com.securechat.common.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class PluginManager {
	private Sides side;
	private Map<String, PluginInstance> plugins;
	private Map<String, HookInstance> hooks;
	private Map<Hooks, List<HookInstance>> hookCache;

	public PluginManager(Sides side) {
		this.side = side;
		plugins = new HashMap<String, PluginInstance>();
		hooks = new HashMap<String, HookInstance>();
		hookCache = new HashMap<Hooks, List<HookInstance>>();
	}

	public void invokeHook(Hooks hook, Object... objects) {
		for (HookInstance inst : hookCache.get(hook)) {
			inst.invoke(objects);
		}
	}

	public void loadPlugins() {
		List<String> classes = getClasses();
		List<String> loadedClasses = new ArrayList<String>();

		for (String clazzName : classes) {
			Class<?> clazz = safeLoad(clazzName);
			if (clazz == null) {
				continue;
			}

			if (!clazz.isAnnotationPresent(Plugin.class)) {
				continue;
			}

			Plugin pluginAnnotation = clazz.getDeclaredAnnotation(Plugin.class);
			loadedClasses.add(clazz.getName());

			PluginInstance instance = new PluginInstance(pluginAnnotation, clazz);
			instance.createInstance();

			plugins.put(instance.getName(), instance);
			System.out.println("Found plugin " + instance.getFullString());

			for (Method method : clazz.getDeclaredMethods()) {
				if (!method.isAnnotationPresent(Hook.class)) {
					continue;
				}
				Hook hookAnnotation = method.getDeclaredAnnotation(Hook.class);
				if (Modifier.isStatic(method.getModifiers())) {
					System.out.println("Hooks can not be on static methods!");
					continue;
				}

				method.setAccessible(true);

				HookInstance hook = new HookInstance(instance, hookAnnotation, method);

				if (hooks.containsKey(hook.getName())) {
					throw new RuntimeException("Hook already exists!");
				}
				hooks.put(hook.getName(), hook);
				System.out.println("Found hook " + hook.getName());
			}
		}
	}

	public void regeneateCache() {
		hookCache.clear();
		for (Hooks hook : Hooks.values()) {
			List<HookInstance> insts = generateHook(hook);
			hookCache.put(hook, insts);

			System.out.println("Plugin hooks cache rebuilt for " + hook + "!");
			for (int i = 0; i < insts.size(); i++) {
				System.out.println(i + ": " + (insts.get(i) != null ? insts.get(i).getName() : "root"));
			}
		}

	}

	private List<HookInstance> generateHook(Hooks hook) {
		Map<String, HookNode> nodeMap = new HashMap<String, HookNode>();

		for (HookInstance inst : hooks.values()) {
			if (side.allows(inst.getSide())) {
				nodeMap.put(inst.getName(), new HookNode(inst));
			}
		}

		for (HookInstance inst : hooks.values()) {
			if (!side.allows(inst.getSide())) {
				continue;
			}
			HookNode node = nodeMap.get(inst.getName());

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

		List<String> notLoaded = new ArrayList<String>();
		notLoaded.addAll(nodeMap.keySet());
		for (HookNode node : nodeMap.values()) {
			for (HookNode dep : node.getDependencies()) {
				notLoaded.remove(dep.getInstance().getName());
			}
		}

		HookNode root = new HookNode(null);
		for (String name : notLoaded) {
			root.addDependency(nodeMap.get(name));
		}

		List<HookNode> resolved = new LinkedList<HookNode>();
		List<HookNode> unresolved = new ArrayList<HookNode>();

		resolveDependencies(root, resolved, unresolved);

		resolved.remove(root);

		List<HookInstance> result = new LinkedList<HookInstance>();

		for (HookNode node : resolved) {
			result.add(node.getInstance());
		}

		return result;
	}

	private void resolveDependencies(HookNode node, List<HookNode> resolved, List<HookNode> unresolved) {
		unresolved.add(node);
		for (HookNode dep : node.getDependencies()) {
			if (!resolved.contains(dep)) {
				if (unresolved.contains(dep))
					throw new RuntimeException(
							"Circular dependency detected! " + node.getName() + " to " + dep.getName());
				resolveDependencies(dep, resolved, unresolved);
			}
		}
		resolved.add(node);
		unresolved.add(node);
	}

	private Class<?> safeLoad(String name) {
		try {
			return Class.forName(name);
		} catch (Exception e) {
			return null;
		}
	}

	private List<String> getClasses() {
		List<String> classes = new ArrayList<String>();
		List<String> paths;

		String javaClassPath = System.getProperty("java.class.path");
		if (javaClassPath != null) {
			paths = Arrays.asList(javaClassPath.split(File.pathSeparator));
		} else {
			paths = new ArrayList<String>();
		}

		for (String path : paths) {
			try {
				if (path.endsWith(".jar")) {
					JarInputStream jis = new JarInputStream(new FileInputStream(path));
					ZipEntry entry = null;
					while ((entry = jis.getNextJarEntry()) != null) {
						if (entry.getName().endsWith(".class")) {
							String name = entry.getName();
							name = name.replaceAll("/", ".");
							name = name.replaceAll("\\\\", ".");
							name = name.replace(".class", "");
							classes.add(name);
						}
					}
					jis.close();
				} else {
					File sDir = new File(path).getAbsoluteFile();
					String basePath = sDir.getAbsolutePath();
					List<String> found = Files.walk(Paths.get(sDir.toURI())).filter(Files::isRegularFile)
							.filter(p -> p.toString().endsWith(".class")).map(p -> p.toFile().getAbsolutePath())
							.map(s -> s.substring(basePath.length() + 1))
							.map(s -> s.replaceAll("/", ".").replaceAll("\\\\", ".").replace(".class", ""))
							.collect(Collectors.toList());
					classes.addAll(found);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return classes;
	}

}
