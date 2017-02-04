package com.securechat.plugins.swtgui.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.securechat.api.common.network.IConnectionProfile;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class InitialConnectionShell extends Shell {
	private Text username;

	public InitialConnectionShell(LoginShell loginShell, IConnectionProfile profile) {
		super(loginShell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setText("Secure Chat - New Account");
		setLayout(new FormLayout());

		Label lblInfo = new Label(this, SWT.WRAP);
		FormData fd_lblInfo = new FormData();
		fd_lblInfo.right = new FormAttachment(0, 440);
		fd_lblInfo.top = new FormAttachment(0, 10);
		fd_lblInfo.left = new FormAttachment(0, 10);
		lblInfo.setLayoutData(fd_lblInfo);
		lblInfo.setText("Please choose a username for use on this server");

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(lblInfo, 0, SWT.RIGHT);
		fd_composite.top = new FormAttachment(lblInfo, 6);
		fd_composite.left = new FormAttachment(lblInfo, 0, SWT.LEFT);
		composite.setLayoutData(fd_composite);

		Label lblUsername = new Label(composite, SWT.NONE);
		lblUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsername.setText("Username");

		username = new Text(composite, SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblstatus = new Label(composite, SWT.NONE);
		lblstatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblstatus.setText("Status");

		Label lblStatusValue = new Label(composite, SWT.WRAP);
		lblStatusValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite, SWT.NONE);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));

		Button btnCreate = new Button(composite_1, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				create();
			}
		});
		btnCreate.setBounds(0, 0, 97, 29);
		btnCreate.setText("Create Account");
		setDefaultButton(btnCreate);

		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		btnCancel.setBounds(0, 0, 97, 29);
		btnCancel.setText("Cancel");

		Point size = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		setSize(size);
	}

	private void create(){
		
	}
	
	@Override
	protected void checkSubclass() {
	}

}
