package com.securechat.plugins.swtgui;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.jdt.internal.jarinjarloader.RsrcURLStreamHandlerFactory;

import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;
import com.securechat.api.common.plugins.InjectInstance;

/**
 * The loader called by the plugin manager to load the required natives.
 *
 */
public class SWTLoader {
	private static final String SWT_VERSION = "4.6.2";
	@InjectInstance
	private ILogger log;
	@InjectInstance
	private IContext context;

	public void load() {
		try {
			if (context.getSide() != Sides.Client) {
				log.warning("SWTGui should not be installed on a server!");
			}
			log.info("Attempting to load SWT");
			String file = "swt-" + context.getOsType() + context.getPlatformArch() + "-" + SWT_VERSION + ".jar";
			log.info("SWT jar: " + file);

			// Gets the current class loader
			URLClassLoader cl = (URLClassLoader) SWTLoader.class.getClassLoader();

			// Using reflection, gets the addURL method
			URL.setURLStreamHandlerFactory(new RsrcURLStreamHandlerFactory(cl));
			Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addUrlMethod.setAccessible(true);

			// Loads the jar
			addUrlMethod.invoke(cl, new URL("rsrc:" + file));

			// Sets the current class loader
			Thread.currentThread().setContextClassLoader(cl);

			log.info("Loaded SWT");
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}

}
