package com.securechat.plugins.swtgui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import com.securechat.api.client.gui.IGui;
import com.securechat.api.client.gui.IGuiProvider;
import com.securechat.api.client.gui.IKeystoreGui;
import com.securechat.api.client.gui.IMainGui;
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

/**
 * An official plugin providing the SWT based GUI.
 */
@Plugin(name = SWTGuiPlugin.NAME, version = SWTGuiPlugin.VERSION, side = Sides.Client)
public class SWTGuiPlugin implements IGuiProvider {
	public static final String NAME = "official-swt_gui", VERSION = "1.0.0";
	@InjectInstance
	private IContext context;
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
	public IMainGui getMainGui() {
		if (mainGui == null) {
			mainGui = new MainGui(this);
			factory.inject(mainGui);
		}
		return mainGui;
	}

	@Override
	public void handleCrash(Throwable reason) {
		display.syncExec(() -> {
			// Converts stack trace to a list of lines
			StringWriter traceWriter = new StringWriter();
			PrintWriter pw = new PrintWriter(traceWriter);
			reason.printStackTrace(pw);
			String trace = traceWriter.toString();

			List<Status> childStatuses = new ArrayList<>();
			for (String line : trace.split(System.getProperty("line.separator"))) {
				childStatuses.add(new Status(IStatus.ERROR, "client", line));
			}

			// Displays the error dialog
			MultiStatus ms = new MultiStatus("client", IStatus.ERROR, childStatuses.toArray(new Status[] {}),
					reason.getLocalizedMessage(), reason);
			ErrorDialog.openError(null, "Secure Chat - Crash",
					"Sorry! Secure chat client has crashed and will now close.", ms);
		});
		context.exit();
	}

	/**
	 * Executes the method on the GUI thread.
	 * 
	 * @param run
	 *            the method to run
	 */
	public void async(Runnable run) {
		display.asyncExec(() -> {
			try {
				run.run();
			} catch (Exception e) {
				context.handleCrash(e);
			}
		});
	}

	/**
	 * Executes the method on the GUI thread.
	 * 
	 * @param run
	 *            the method to run
	 */
	public void sync(Runnable run) {
		display.syncExec(() -> {
			try {
				run.run();
			} catch (Exception e) {
				context.handleCrash(e);
			}
		});
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

	public static final ImplementationMarker PROVIDER_MARKER;
	static {
		PROVIDER_MARKER = new ImplementationMarker(NAME, VERSION, "swt_gui", "1.0.0");
	}

}
