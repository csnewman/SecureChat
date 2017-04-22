package com.securechat.plugins.swtgui.keystore;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import com.securechat.api.client.gui.IKeystoreGui;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.security.IKeystore;
import com.securechat.plugins.swtgui.GuiBase;
import com.securechat.plugins.swtgui.SWTGuiPlugin;

/**
 * A SWT based keystore loading/creation gui.
 */
public class KeystoreGui extends GuiBase implements IKeystoreGui {
	private boolean exists;
	private IKeystore keystore;
	private KeystoreShell shell;
	private boolean completed;

	public KeystoreGui(SWTGuiPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init(IKeystore keystore) {
		this.keystore = keystore;
		exists = keystore.exists();
	}

	@Override
	protected void createShell() {
		shell = new KeystoreShell(plugin.getDisplay());
		shell.getBtnMain().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onMain();
			}
		});
		shell.getBtnExit().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				completed = false;
				close();
			}
		});

	}

	@Override
	protected void onOpen() {
		shell.updateInfo(!exists);
	}

	private void onMain() {
		// Attempts to load/create the keystore
		if (exists) {
			if (keystore.load(shell.getPassword())) {
				completed = true;
				close();
				return;
			} else {
				shell.setMessage("Invalid password");
			}
		} else {
			if (keystore.generate(shell.getPassword())) {
				completed = true;
				close();
				return;
			}
			shell.setMessage("Failed to generate");
		}
	}

	@Override
	protected void onClose() {
		if (!completed) {
			context.exit();
		}
	}

	@Override
	public Shell getShell() {
		return shell;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

	public static final ImplementationMarker MARKER;
	static {
		MARKER = new ImplementationMarker(SWTGuiPlugin.NAME, SWTGuiPlugin.VERSION, "keystore_gui", "1.0.0");
	}

}
