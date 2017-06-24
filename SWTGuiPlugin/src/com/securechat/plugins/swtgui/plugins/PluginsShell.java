package com.securechat.plugins.swtgui.plugins;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.securechat.api.common.IContext;
import com.securechat.api.common.plugins.IPluginInstance;
import com.securechat.api.common.plugins.IPluginManager;
import com.securechat.api.common.storage.IStorage;
import com.securechat.plugins.swtgui.login.LoginGui;
import com.securechat.plugins.swtgui.login.LoginShell;

public class PluginsShell extends Shell {
	private IContext context;
	private IPluginManager pluginManager;
	private IStorage storage;
	private String lastPath;
	private Table table;

	public PluginsShell(LoginShell loginShell, LoginGui loginGui) {
		super(loginShell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setText("Secure Chat - Plugins");
		setSize(450, 431);

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 52, 414, 303);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(309);
		tblclmnName.setText("Name");

		TableColumn tblclmnVersion = new TableColumn(table, SWT.NONE);
		tblclmnVersion.setWidth(100);
		tblclmnVersion.setText("Version");

		Label lblInfo = new Label(this, SWT.WRAP | SWT.HORIZONTAL);
		lblInfo.setText(
				"The list of installed plugins is below. You can install a new plugin by clicking install new plugin.");
		lblInfo.setBounds(10, 10, 414, 36);

		Button btnInstallNewPlugin = new Button(this, SWT.NONE);
		btnInstallNewPlugin.setBounds(298, 361, 126, 25);
		btnInstallNewPlugin.setText("Install New Plugin");

		context = loginGui.getContext();
		pluginManager = context.getPluginManager();
		storage = context.getStorage();

		// Adds a row for each plugin
		for (IPluginInstance instance : pluginManager.getPlugins()) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] { instance.getName(), instance.getVersion() });
		}

		btnInstallNewPlugin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Opens file selection dialog with the scplugin filter
				FileDialog dialog = new FileDialog(PluginsShell.this, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.scplugin" });
				dialog.setFilterNames(new String[] { "Secure Chat Plugin (*.scplugin)" });
				if (lastPath != null)
					dialog.setFilterPath(lastPath);
				String result = dialog.open();

				if (result != null) {
					lastPath = dialog.getFilterPath();
					doInstall(result);
				}
			}
		});
	}

	private void doInstall(String path) {
		storage.installPlugin(path);
		MessageDialog dialog = new MessageDialog(this, "Secure Chat - Plugin Installed", null,
				"Your plugin has been installed, however your plugin will not be loaded until the next restart of the program.\nPlugin Path: "
						+ path,
				MessageDialog.INFORMATION, new String[] { "Exit Later", "Exit Now" }, 1);
		if (dialog.open() == 1) {
			context.exit();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
