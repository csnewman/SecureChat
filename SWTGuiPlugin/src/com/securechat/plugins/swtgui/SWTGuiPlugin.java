package com.securechat.plugins.swtgui;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import com.securechat.api.client.gui.IGui;
import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.client.gui.IKeystoreGui;
import com.securechat.api.common.IContext;
import com.securechat.api.common.Sides;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.plugins.Plugin;
import com.securechat.plugins.swtgui.keystore.KeystoreGui;
import com.securechat.plugins.swtgui.login.LoginGui;
import com.securechat.plugins.swtgui.main.MainGui;

@Plugin(name = SWTGuiPlugin.NAME, version = SWTGuiPlugin.VERSION, side = Sides.Client)
public class SWTGuiPlugin implements IGuiProvider {
	public static final String NAME = "official-swt_gui", VERSION = "1.0.0";
	public static final ImplementationMarker PROVIDER_MARKER = new ImplementationMarker(NAME, VERSION, "swt_gui",
			"1.0.0");
	@InjectInstance
	private IImplementationFactory factory;
	private Display display;
	private LoginGui loginGui;
	private KeystoreGui keystoreGui;
	private MainGui mainGui;

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		IImplementationFactory factory = context.getImplementationFactory();
		factory.registerInstance(PROVIDER_MARKER, IGuiProvider.class, this);
		factory.setFallbackDefaultIfNone(IGuiProvider.class, PROVIDER_MARKER);
	}

	@Override
	public void init(Runnable ready) {
		display = Display.getDefault();
		new Thread(ready).start();
		while (!display.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	@Override
	public IKeystoreGui getKeystoreGui() {
		if (keystoreGui == null) {
			keystoreGui = new KeystoreGui(this);
			factory.inject(keystoreGui);
		}
		return keystoreGui;
	}

	@Override
	public IGui getLoginGui() {
		if (loginGui == null) {
			loginGui = new LoginGui(this);
			factory.inject(loginGui);
		}
		return loginGui;
	}
	
	@Override
	public IGui getMainGui() {
		if(mainGui == null){
			mainGui = new MainGui(this);
			factory.inject(mainGui);
		}
		return mainGui;
	}

	public void async(Runnable run) {
		display.asyncExec(run);
	}

	public void sync(Runnable run) {
		display.syncExec(run);
	}

	public Font getFont(int size, int style) {
		return SWTResourceManager.getFont(display.getSystemFont().getFontData()[0].getName(), size, style);
	}

	public Display getDisplay() {
		return display;
	}

	@Override
	public ImplementationMarker getMarker() {
		return PROVIDER_MARKER;
	}

}
