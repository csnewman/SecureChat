package com.securechat.plugins.swtgui.plugins;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.securechat.api.common.IContext;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.IImplementationInstance;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.plugins.swtgui.login.LoginGui;
import com.securechat.plugins.swtgui.login.LoginShell;

public class ImplementationsShell extends Shell {
	private IContext context;
	private Table table;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ImplementationsShell(LoginShell loginShell, LoginGui loginGui) {
		super(loginShell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setText("Secure Chat - Implementations");
		setSize(450, 425);

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 74, 414, 303);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnImplementation = new TableColumn(table, SWT.NONE);
		tblclmnImplementation.setWidth(129);
		tblclmnImplementation.setText("Implementation");

		TableColumn tblclmnProvider = new TableColumn(table, SWT.NONE);
		tblclmnProvider.setWidth(281);
		tblclmnProvider.setText("Provider");

		context = loginGui.getContext();
		IImplementationFactory factory = context.getImplementationFactory();

		Map<Class, Map<String, IImplementationInstance>> imps = factory.getAllImplementations();

		for (Entry<Class, Map<String, IImplementationInstance>> e : imps.entrySet()) {

			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] { e.getKey().getSimpleName(), "" });

			TableEditor editor = new TableEditor(table);
			CCombo combo = new CCombo(table, SWT.NONE);

			ImplementationMarker defaultImp = factory.getDefault(e.getKey());
			combo.setText(defaultImp.getName() + " - " + defaultImp.getVersion() + " - " + defaultImp.getPluginName()
					+ " - " + defaultImp.getPluginVersion());

			for (IImplementationInstance inst : e.getValue().values()) {
				ImplementationMarker marker = inst.getMarker();
				combo.add(marker.getName() + " - " + marker.getVersion() + " - " + marker.getPluginName() + " - "
						+ marker.getPluginVersion());
			}

			combo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					for (IImplementationInstance inst : e.getValue().values()) {
						ImplementationMarker marker = inst.getMarker();
						if (arg0.text.equals(marker.getName() + " - " + marker.getVersion() + " - "
								+ marker.getPluginName() + " - " + marker.getPluginVersion())) {
							factory.setDefault(e.getKey(), marker);

							MessageDialog dialog = new MessageDialog(ImplementationsShell.this,
									"Secure Chat - Implementation Updated", null,
									"The default implementation has been updated, however this change will not fully take effect until the next restart of the program..",
									MessageDialog.INFORMATION, new String[] { "Exit Later", "Exit Now" }, 1);
							if (dialog.open() == 1) {
								context.exit();
							}
						}
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
			});

			editor.grabHorizontal = true;
			editor.setEditor(combo, tableItem, 1);
		}

		Label lblInfo = new Label(this, SWT.WRAP | SWT.HORIZONTAL);
		lblInfo.setText(
				"The list of in use implementations is below. Please be careful when changing used implementations as it may cause issues connecting to servers or may cause the program to become unusable.");
		lblInfo.setBounds(10, 10, 414, 58);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
