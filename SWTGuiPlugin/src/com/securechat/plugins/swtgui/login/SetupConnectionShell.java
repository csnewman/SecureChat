package com.securechat.plugins.swtgui.login;

import java.util.Collection;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.IImplementationInstance;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IPasswordEncryption;
import com.securechat.api.common.storage.IStorage;

public class SetupConnectionShell extends Shell {
	private LoginGui loginGui;
	private Text profilePath;
	private Text password;
	private String lastPath;
	private Button btnImport;
	private Label lblStatusValue, lblNameValue, lblHostValue;
	private Combo providersCombo;
	private IConnectionProfileProvider[] providers;
	@InjectInstance
	private IStorage storage;
	@InjectInstance
	private IImplementationFactory factory;
	private IConnectionProfileProvider profileProvider;
	private IConnectionProfile profile;
	private LoginShell loginShell;

	public SetupConnectionShell(LoginShell loginShell, LoginGui loginGui) {
		super(loginShell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		this.loginShell = loginShell;
		this.loginGui = loginGui;
		setText("Secure Chat - New Connection");
		setLayout(new FormLayout());

		Label lblInfo = new Label(this, SWT.WRAP);
		FormData fd_lblInfo = new FormData();
		fd_lblInfo.right = new FormAttachment(0, 440);
		fd_lblInfo.top = new FormAttachment(0, 10);
		fd_lblInfo.left = new FormAttachment(0, 10);
		lblInfo.setLayoutData(fd_lblInfo);
		lblInfo.setText("A secure connection profile file is required.\nYou can get this file from the server host.");

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(0, 440);
		fd_composite.top = new FormAttachment(lblInfo, 6);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);

		Label lblProfilePath = new Label(composite, SWT.NONE);
		lblProfilePath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProfilePath.setText("Profile Path");

		profilePath = new Text(composite, SWT.BORDER);
		GridData gd_profilePath = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_profilePath.minimumWidth = 200;
		profilePath.setLayoutData(gd_profilePath);

		Button btnPathDialog = new Button(composite, SWT.NONE);
		btnPathDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(SetupConnectionShell.this, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.sccp" });
				dialog.setFilterNames(new String[] { "Secure Chat Connection Profile (*.sccp)" });
				if (lastPath != null)
					dialog.setFilterPath(lastPath);
				String result = dialog.open();

				if (result != null) {
					profilePath.setText(result);
					lastPath = dialog.getFilterPath();
				}
			}
		});
		btnPathDialog.setText("...");

		Label lblPassword = new Label(composite, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password");

		password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Provider");

		providersCombo = new Combo(composite, SWT.NONE);
		providersCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		IImplementationFactory factory = loginGui.getContext().getImplementationFactory();
		Collection<IImplementationInstance<? extends IConnectionProfileProvider>> implementations = factory
				.getImplementations(IConnectionProfileProvider.class).values();
		String[] names = new String[implementations.size()];
		providers = new IConnectionProfileProvider[implementations.size()];
		int i = 0;
		for (IImplementationInstance<? extends IConnectionProfileProvider> implemetation : implementations) {
			IConnectionProfileProvider provider = implemetation.provide();
			names[i] = provider.getDisplayName();
			providers[i] = provider;
			i++;
		}
		providersCombo.setItems(names);

		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));

		Button btnDecrypt = new Button(composite_1, SWT.NONE);
		btnDecrypt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				decrypt();
			}
		});
		btnDecrypt.setBounds(0, 0, 97, 29);
		btnDecrypt.setText("Decrypt");

		btnImport = new Button(composite_1, SWT.NONE);
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(profile != null){
					doImport();
				}
			}
		});
		btnImport.setText("Import");
		new Label(composite, SWT.NONE);

		Label lblStatus = new Label(composite, SWT.NONE);
		lblStatus.setText("Status");

		lblStatusValue = new Label(composite, SWT.NONE);
		lblStatusValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite, SWT.NONE);

		Label lblName = new Label(composite, SWT.NONE);
		lblName.setText("Name");

		lblNameValue = new Label(composite, SWT.NONE);
		lblNameValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite, SWT.NONE);

		Label lblHost = new Label(composite, SWT.NONE);
		lblHost.setText("Host");

		lblHostValue = new Label(composite, SWT.NONE);
		lblHostValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite, SWT.NONE);

		setStatus(false, "");

		Point size = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		setSize(size);
	}

	private void doImport(){
		close();
		InitialConnectionShell connectionShell = new InitialConnectionShell(loginShell, profileProvider, profile, loginGui);
		factory.inject(connectionShell);
		connectionShell.layout();
		connectionShell.open();
	}
	
	private void decrypt() {
		if(providersCombo.getSelectionIndex() < 0){
			setStatus(false, "No provider selected");
			profile = null;
			return;
		}
		profileProvider = providers[providersCombo.getSelectionIndex()];
		
		String path = profilePath.getText();
		if (storage.doesFileExist(path)) {
			try {
				IPasswordEncryption encryption = factory.provide(IPasswordEncryption.class);
				encryption.init(password.getTextChars());

				profile = profileProvider.loadProfileFromFile(storage, path, encryption);

				if (profile == null) {
					setStatus(false, "Failed to decrypt");
					profile = null;
				}

				if (!profile.isTemplate()) {
					setStatus(false, "Profile is not a template");
					profile = null;
					return;
				}

				setStatus(true, "Decrypted");
				lblNameValue.setText(profile.getName());
				lblHostValue.setText(profile.getIP() + ":" + profile.getPort());
			} catch (Exception ex) {
				ex.printStackTrace();
				setStatus(false, "Failed to decrypt");
				profile = null;
			}
		} else {
			setStatus(false, "File not found");
			profile = null;
		}
	}

	private void setStatus(boolean ok, String text) {
		lblStatusValue.setText(text);
		btnImport.setEnabled(ok);
		if (!ok) {
			lblNameValue.setText("");
			lblHostValue.setText("");
		}
	}

	@Override
	protected void checkSubclass() {
	}
}
