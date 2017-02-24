package com.securechat.plugins.swtgui.main;

import org.eclipse.swt.widgets.Shell;

import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.plugins.swtgui.GuiBase;
import com.securechat.plugins.swtgui.SWTGuiPlugin;

public class MainGui extends GuiBase {
	public static final ImplementationMarker MARKER = new ImplementationMarker(SWTGuiPlugin.NAME, SWTGuiPlugin.VERSION,
			"main_gui", "1.0.0");
	private MainShell shell;

	public MainGui(SWTGuiPlugin plugin) {
		super(plugin);
	}

	@Override
	protected void createShell() {
		shell = new MainShell(plugin.getDisplay(), this);
	}

	@Override
	protected void onOpen() {
		System.out.println("OPEN!!");
	}

	@Override
	protected void onClose() {
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
