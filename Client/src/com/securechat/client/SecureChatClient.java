package com.securechat.client;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import com.securechat.client.connection.ConnectionStore;
import com.securechat.client.connection.LoginWindow;
import com.securechat.common.security.PasswordEncryption;
import com.securechat.common.security.ProtectedKeyStore;
import com.securechat.common.security.SecurityUtils;

public class SecureChatClient {
	private static final File keystoreFile = new File("keystore.bin");
	private static SecureChatClient INSTANCE;
	private ProtectedKeyStore keyStore;
	private LoginWindow loginWindow;
	private ConnectionStore connectionStore;

	public void init() {
		loginWindow = new LoginWindow(INSTANCE);
		loginWindow.open();

		if (keystoreFile.exists()) {
			unlockKeyStore();
			connectionStore = new ConnectionStore(keyStore.loadKeyPair("connections"));
		} else {
			generateKeyStore();
			connectionStore = new ConnectionStore(keyStore.generateKeyPair("connections"));
		}

		loginWindow.updateOptions();

	}

	private void unlockKeyStore() {
		boolean unlocked = false;
		while (!unlocked) {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Keystore Password:");
			panel.add(label);
			JPasswordField pass = new JPasswordField(20);
			panel.add(pass);

			String[] options = new String[] { "Unlock", "Quit" };
			int option = JOptionPane.showOptionDialog(loginWindow.getFrame(), panel, "Unlock KeyStore",
					JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				try {
					keyStore = new ProtectedKeyStore(keystoreFile,
							new PasswordEncryption(SecurityUtils.secureHashChars(pass.getPassword())));
					keyStore.load();
					unlocked = true;
				} catch (RuntimeException e) {
					e.printStackTrace();
					continue;
				}
			} else {
				System.exit(-1);
			}
		}
	}

	private void generateKeyStore() {
		char[] password = null;
		boolean gotPassword = false;
		while (!gotPassword) {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("New Keystore Password:");
			panel.add(label);
			JPasswordField pass = new JPasswordField(20);
			panel.add(pass);

			String[] options = new String[] { "Lock", "Quit" };

			int option = JOptionPane.showOptionDialog(loginWindow.getFrame(), panel, "New KeyStore",
					JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				password = pass.getPassword();
				if (password.length == 0) {
					continue;
				}
				gotPassword = true;
			} else {
				System.exit(-1);
			}
		}
		keyStore = new ProtectedKeyStore(keystoreFile, new PasswordEncryption(SecurityUtils.secureHashChars(password)));
		keyStore.save();
	}

	public ConnectionStore getConnectionStore() {
		return connectionStore;
	}

	public static void main(String[] args) {
		INSTANCE = new SecureChatClient();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					INSTANCE.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// KeyPair pair = SecurityUtils.generateKeyPair();
		//
		// System.out.println("Public: " +
		// Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
		// System.out.println("Private: " + new
		// String(pair.getPrivate().getEncoded()));
		//
		//
		// long start = System.currentTimeMillis();
		// System.out.println("Encrypted: " + new
		// String(SecurityUtils.encryptData("Hello".getBytes(),
		// pair.getPublic())));
		// System.out.println(System.currentTimeMillis() - start);
		//// SecurityUtils.hashData(input)

		// new NetworkClient().connect("127.0.0.1", 1234);

	}

}
