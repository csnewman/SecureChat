package com.securechat.plugins.swtgui.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class LoginShell extends Shell {
	private Label lblServerHostValue;
	private Label lblServerNameValue;
	private Combo connectionsCombo;
	private Button btnConnect;

	public LoginShell(Display display) {
		super(display, SWT.CLOSE | SWT.TITLE);
		setText("Secure Chat - Login");
		setLayout(new FormLayout());

		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);

		MenuItem mntmFileSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmFileSubmenu.setText("File");

		Menu menu_1 = new Menu(mntmFileSubmenu);
		mntmFileSubmenu.setMenu(menu_1);

		MenuItem mntmImportConnection = new MenuItem(menu_1, SWT.NONE);
		mntmImportConnection.setText("Import Connection");

		Label lblTitle = new Label(this, SWT.NONE);
		lblTitle.setAlignment(SWT.CENTER);
		lblTitle.setFont(SWTResourceManager.getFont("Ubuntu", 20, SWT.NORMAL));
		FormData fd_lblTitle = new FormData();
		fd_lblTitle.right = new FormAttachment(100, -10);
		fd_lblTitle.top = new FormAttachment(0, 10);
		fd_lblTitle.left = new FormAttachment(0, 10);
		lblTitle.setLayoutData(fd_lblTitle);
		lblTitle.setText("Secure Chat");

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 10;
		composite.setLayout(gl_composite);
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(lblTitle, 0, SWT.RIGHT);
		fd_composite.top = new FormAttachment(lblTitle, 6);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);

		Label lblConnection = new Label(composite, SWT.NONE);
		lblConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnection.setText("Connection");

		connectionsCombo = new Combo(composite, SWT.NONE);
//		connectionsCombo.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				updateSelection();
//			}
//		});
		GridData gd_connectionsCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_connectionsCombo.minimumWidth = 200;
		connectionsCombo.setLayoutData(gd_connectionsCombo);

		Label lblServerName = new Label(composite, SWT.NONE);
		lblServerName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerName.setText("Server Name");

		lblServerNameValue = new Label(composite, SWT.NONE);
		lblServerNameValue.setText("New Label");

		Label lblServerHost = new Label(composite, SWT.NONE);
		lblServerHost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerHost.setText("Server Host");

		lblServerHostValue = new Label(composite, SWT.NONE);
		lblServerHostValue.setText("New Label");
		new Label(composite, SWT.NONE);

		btnConnect = new Button(composite, SWT.NONE);
		btnConnect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnConnect.setText("Connect");

		Point size = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		setSize(size);
	}

	@Override
	protected void checkSubclass() {
	}

	public Label getLblServerHostValue() {
		return lblServerHostValue;
	}

	public Label getLblServerNameValue() {
		return lblServerNameValue;
	}

	public Combo getConnectionsCombo() {
		return connectionsCombo;
	}

	public Button getBtnConnect() {
		return btnConnect;
	}

}
