package com.securechat.plugins.swtgui.login;

import org.eclipse.swt.widgets.Shell;

import com.securechat.api.client.network.IConnectionStore;
import com.securechat.api.client.network.IConnectionStoreUpdateListener;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.plugins.swtgui.GuiBase;
import com.securechat.plugins.swtgui.SWTGuiPlugin;

public class LoginGui extends GuiBase implements IConnectionStoreUpdateListener {
	public static final ImplementationMarker MARKER = new ImplementationMarker(SWTGuiPlugin.NAME, SWTGuiPlugin.VERSION,
			"login_gui", "1.0.0");
	@InjectInstance
	private IConnectionStore connectionStore;
	private IConnectionProfile[] profiles;
	private LoginShell shell;

	public LoginGui(SWTGuiPlugin plugin) {
		super(plugin);
	}

	@Override
	protected void createShell() {
		shell = new LoginShell(plugin.getDisplay());
	}

	@Override
	protected void onOpen() {
		connectionStore.addUpdateListener(this);
		updateOptions();
	}

	@Override
	public void onConnectionStoreUpdated() {
		plugin.sync(this::updateOptions);
	}

	private void updateOptions() {
		System.out.println("Update Options");
		profiles = connectionStore.getProfiles().toArray(new IConnectionProfile[0]);
		String[] names = new String[profiles.length];
		for (int i = 0; i < profiles.length; i++) {
			IConnectionProfile profile = profiles[i];
			names[i] = profile.getName() + "(" + profile.getUsername() + ")";
		}
		shell.getConnectionsCombo().setItems(names);
		updateSelection();
	}

	private void updateSelection() {
		System.out.println("Selection Update");
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

	@Override
	protected void onClose() {
		connectionStore.removeUpdateListener(this);
		context.exit();
	}

	@Override
	public Shell getShell() {
		return shell;
	}
	
	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}
}
