package com.securechat.plugins.swtgui.keystore;

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

/**
 * The actual SWT shell for the keystore gui.
 */
public class KeystoreShell extends Shell {
	private Label lblInfo, lblMessage;
	private Text passwordInput;
	private Button btnMain, btnExit;

	public KeystoreShell(Display display) {
		super(display, SWT.CLOSE | SWT.TITLE);
		setText("Secure Chat - Keystore");
		setLayout(new FormLayout());

		lblInfo = new Label(this, SWT.WRAP);
		FormData fd_lblInfo = new FormData();
		fd_lblInfo.right = new FormAttachment(100, -10);
		fd_lblInfo.top = new FormAttachment(0, 10);
		fd_lblInfo.left = new FormAttachment(0, 10);
		lblInfo.setLayoutData(fd_lblInfo);

		Composite form = new Composite(this, SWT.NONE);
		form.setLayout(new GridLayout(2, false));
		FormData fd_form = new FormData();
		fd_form.right = new FormAttachment(lblInfo, 0, SWT.RIGHT);
		fd_form.top = new FormAttachment(lblInfo, 6);
		fd_form.left = new FormAttachment(0, 10);
		form.setLayoutData(fd_form);

		new Label(form, SWT.NONE).setText("Password");

		passwordInput = new Text(form, SWT.BORDER | SWT.PASSWORD);
		passwordInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(form, SWT.NONE);
		lblMessage = new Label(form, SWT.NONE);
		lblMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnExit = new Button(this, SWT.NONE);

		FormData fd_btnExit = new FormData();
		fd_btnExit.bottom = new FormAttachment(100, -10);
		fd_btnExit.right = new FormAttachment(lblInfo, 0, SWT.RIGHT);
		btnExit.setLayoutData(fd_btnExit);
		btnExit.setText("Exit");

		btnMain = new Button(this, SWT.NONE);
		setDefaultButton(btnMain);
		FormData fd_btnMain = new FormData();
		fd_btnMain.bottom = new FormAttachment(btnExit, 0, SWT.BOTTOM);
		fd_btnMain.right = new FormAttachment(btnExit, -6);
		btnMain.setLayoutData(fd_btnMain);

		// Temp size calcs
		updateInfo(true);
		updateSize();
	}

	public void updateInfo(boolean generate) {
		if (generate) {
			lblInfo.setText("Welcome to Secure Chat, as no keystore exists you will need to generate one now.\n"
					+ "The keystore will contain all of your keys so a secure password is advisable.");
			btnMain.setText("Create");
		} else {
			lblInfo.setText("Welcome to Secure Chat, as your keystore exists you will need to unlock it now.\n"
					+ "You will need the password you used to generate it to unlock it.");
			btnMain.setText("Unlock");
		}
		updateSize();
	}

	private void updateSize() {
		Point size = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		size.y += btnMain.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 10;
		setSize(size);
		redraw();
		lblMessage.redraw();
	}

	public Button getBtnMain() {
		return btnMain;
	}

	public Button getBtnExit() {
		return btnExit;
	}

	public void setMessage(String msg) {
		lblMessage.setText(msg);
		updateSize();
	}

	public char[] getPassword() {
		return passwordInput.getTextChars();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
