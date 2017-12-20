package com.securechat.plugins.swtgui.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.securechat.api.client.network.IClientNetworkManager;
import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.client.network.IConnectionStoreUpdateListener;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.plugins.swtgui.GuiBase;
import com.securechat.plugins.swtgui.SWTGuiPlugin;

/**
 * A SWT based login GUI.
 */
public class LoginGui extends GuiBase implements IConnectionStoreUpdateListener {
	@InjectInstance
	private IConnectionStore connectionStore;
	@InjectInstance
	private IClientNetworkManager networkManager;
	private IConnectionProfile[] profiles;
	private LoginShell shell;
	private boolean completed;

	public LoginGui(SWTGuiPlugin plugin) {
		super(plugin);
	}

	@Override
	protected void createShell() {
		shell = new LoginShell(plugin.getDisplay(), this);
	}

	@Override
	protected void onOpen() {
		// Register a connection store listener
		connectionStore.addUpdateListener(this);
		updateOptions();
	}

	@Override
	public void onConnectionStoreUpdated() {
		// Updates server list
		plugin.sync(this::updateOptions);
	}

	/**
	 * Updates the dropdown list of connections
	 */
	private void updateOptions() {
		// Adds each server to the list
		profiles = connectionStore.getProfiles().toArray(new IConnectionProfile[0]);
		String[] names = new String[profiles.length];
		for (int i = 0; i < profiles.length; i++) {
			IConnectionProfile profile = profiles[i];
			// Combine server name and account username to give profile name
			names[i] = profile.getName() + "(" + profile.getUsername() + ")";
		}
		Combo combo = shell.getConnectionsCombo();
		combo.setItems(names);
		if (combo.getSelectionIndex() < 0 && names.length > 0) {
			combo.select(0);
		}
		updateSelection();
	}

	/**
	 * Updates the servers name and host information
	 */
	public void updateSelection() {
		int index = shell.getConnectionsCombo().getSelectionIndex();
		if (profiles.length == 0 || index < 0 || index >= profiles.length) {
			shell.getLblServerNameValue().setText("");
			shell.getLblServerHostValue().setText("");
			shell.getBtnConnect().setEnabled(false);
			return;
		}
		IConnectionProfile profile = profiles[index];
		shell.getLblServerNameValue().setText(profile.getName() + " (" + profile.getUsername() + ")");
		shell.getLblServerHostValue().setText(profile.getIP() + ":" + profile.getPort());
		shell.getBtnConnect().setEnabled(true);
	}

	/**
	 * Starts the connection to the server.
	 */
	public void connect() {
		int index = shell.getConnectionsCombo().getSelectionIndex();
		if (profiles.length == 0 || index < 0 || index >= profiles.length) {
			updateSelection();
			return;
		}
		IConnectionProfile profile = profiles[index];
		shell.setEnabled(false);
		networkManager.connect(profile, (s, r) -> {
			if (s) {
				completed = true;
				close();
			} else {
				plugin.sync(() -> {
					MessageBox messageBox = new MessageBox(shell.isDisposed() ? null : shell, SWT.ICON_ERROR);
					messageBox.setMessage("Failed to connect!\n" + r);
					messageBox.open();
					shell.setEnabled(true);
				});
			}
		});
	}

	@Override
	protected void onClose() {
		connectionStore.removeUpdateListener(this);
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
		MARKER = new ImplementationMarker(SWTGuiPlugin.NAME, SWTGuiPlugin.VERSION, "login_gui", "1.0.0");
	}

}
