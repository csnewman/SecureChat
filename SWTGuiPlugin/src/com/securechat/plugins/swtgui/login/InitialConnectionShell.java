package com.securechat.plugins.swtgui.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.securechat.api.client.network.EnumConnectionSetupStatus;
import com.securechat.api.client.network.IClientNetworkManager;
import com.securechat.api.common.IContext;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.plugins.InjectInstance;

public class InitialConnectionShell extends Shell {
	private LoginGui loginGui;
	@InjectInstance
	private IContext context;
	@InjectInstance
	private IClientNetworkManager networkManager;
	private Text username;
	private Label lblStatusValue;
	private Button btnCancel, btnCreate;
	private IConnectionProfileProvider provider;
	private IConnectionProfile profile;

	public InitialConnectionShell(LoginShell loginShell, IConnectionProfileProvider provider,
			IConnectionProfile profile, LoginGui loginGui) {
		super(loginShell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		this.loginGui = loginGui;
		this.provider = provider;
		this.profile = profile;
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

		lblStatusValue = new Label(composite, SWT.WRAP);
		lblStatusValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite, SWT.NONE);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));

		btnCreate = new Button(composite_1, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				create();
			}
		});
		btnCreate.setBounds(0, 0, 97, 29);
		btnCreate.setText("Create Account");
		setDefaultButton(btnCreate);

		btnCancel = new Button(composite_1, SWT.NONE);
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

	private void create() {
		String usernameValue = username.getText();

		new Thread(() -> {
			try {
				networkManager.setupConnection(provider, profile, usernameValue, this::handleUpdate);
			} catch (Exception e) {
				context.handleCrash(e);
			}
		}).start();
	}

	private void handleUpdate(EnumConnectionSetupStatus status, String msg) {
		switch (status) {
		case GeneratingKeyPair:
			setStatus(false, "Generating keypair");
			break;
		case Connecting:
			setStatus(false, "Connecting");
			break;
		case RegisteringUsername:
			setStatus(false, "Registering username");
			break;
		case Saving:
			setStatus(false, "Saving connection profile");
			break;
		case Success:
			setStatus(false, "Account Created!");
			loginGui.getPlugin().sync(() -> {
				close();
			});
			break;
		case Disconnected:
			setStatus(true, "Disconnected: " + msg);
			break;
		case UsernameTaken:
			setStatus(true, "Username has already been taken! Please try a different one.");
			break;
		default:
			setStatus(false, "Unknown status: " + msg);
			break;
		}

	}

	private void setStatus(boolean enable, String msg) {
		loginGui.getPlugin().sync(() -> {
			username.setEnabled(enable);
			btnCancel.setEnabled(enable);
			btnCreate.setEnabled(enable);
			lblStatusValue.setText(msg);
		});
	}

	@Override
	protected void checkSubclass() {
	}

}
