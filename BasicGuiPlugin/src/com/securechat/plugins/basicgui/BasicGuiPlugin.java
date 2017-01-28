package com.securechat.plugins.basicgui;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.client.gui.ILoginGui;
import com.securechat.api.common.IContext;
import com.securechat.api.common.ILogger;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.api.common.security.IKeystore;

@Plugin(name = BasicGuiPlugin.NAME, version = BasicGuiPlugin.VERSION, side = Sides.Client)
public class BasicGuiPlugin implements IGuiProvider {
	public static final String NAME = "official-basic_gui", VERSION = "1.0.0";
	public static final ImplementationMarker BASIC_GUI_MARKER = new ImplementationMarker(NAME, VERSION, "basic_gui",
			"1.0.0");
	@InjectInstance
	private IImplementationFactory factory;
	private JFrame currentWindow;
	private IContext context;
	private ILogger log;
	private LoginWindow loginGui;

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		this.context = context;
		log = context.getLogger();
		IImplementationFactory factory = context.getImplementationFactory();
		factory.registerInstance(BASIC_GUI_MARKER, IGuiProvider.class, this);
		factory.setFallbackDefaultIfNone(IGuiProvider.class, BASIC_GUI_MARKER);
	}

	@Override
	public void init() {
		try {
			log.debug("Setting system look and feel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showKeystoreGui(IKeystore keystore) {
		if (keystore.isLoaded()) {
			throw new RuntimeException("Keystore is already loaded");
		}

		boolean exists = keystore.exists();
		String msg = null;
		while (true) {
			log.debug("Showing " + (exists ? "unlock" : "generate") + " keystore gui, msg=" + msg);
			KeystoreDialog dialog = new KeystoreDialog(!exists, msg);
			dialog.setVisible(true);

			if (dialog.didComplete()) {
				if (exists) {
					if (keystore.load(dialog.getPassword())) {
						return;
					}
					msg = "Invalid password";
				} else {
					if (keystore.generate(dialog.getPassword())) {
						return;
					}
					msg = "Failed to generate";
				}
			} else {
				context.exit();
			}
		}
	}

	@Override
	public ILoginGui getLoginGui() {
		if (loginGui == null) {
			loginGui = new LoginWindow();
			factory.inject(loginGui);
		}
		return loginGui;
	}

	public JFrame getCurrentWindow() {
		return currentWindow;
	}

	@Override
	public ImplementationMarker getMarker() {
		return BASIC_GUI_MARKER;
	}

}
