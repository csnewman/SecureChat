package com.securechat.client;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import com.securechat.api.common.IContext;
import com.securechat.api.common.properties.PropertyCollection;
import com.securechat.basicencryption.PasswordEncryption;
import com.securechat.client.chat.MainWindow;
import com.securechat.client.connection.ConnectionInfo;
import com.securechat.client.connection.ConnectionStore;
import com.securechat.client.network.NetworkClient;
import com.securechat.common.security.ProtectedKeyStore;
import com.securechat.common.security.SecurityUtils;
import com.securechat.plugins.basicgui.LoginWindow;

public class SecureChatClientOld implements IContext{
	private static final File settingsFile = new File("settings.json");
	private static final File keystoreFile = new File("keystore.bin");
	private static SecureChatClientOld INSTANCE;
	private JFrame currentWindow;
	private ProtectedKeyStore keyStore;
	private ConnectionStore connectionStore;
	private NetworkClient networkClient;
	private PropertyCollection settings;

	public void init() {
		settings = new PropertyCollection(null);
		try{
			settings.loadFile(settingsFile);
		}catch (Exception e) {
		}
		saveSettings();
		
		currentWindow = new LoginWindow(this);
		currentWindow.setVisible(true);

		if (keystoreFile.exists()) {
			unlockKeyStore();
		} else {
			generateKeyStore();
		}
		connectionStore = new ConnectionStore(keyStore.getOrGenKeyPair("connections"));
		connectionStore.tryLoadAndSave();

		keyStore.save();

		getCurrentWindow(LoginWindow.class).updateOptions();
	}
	
	
	@Override
	public PropertyCollection getSettings() {
		return settings;
	}

	@Override
	public void saveSettings() {
		settings.saveToFile(settingsFile);
	}

	public void onConnected(ConnectionInfo info, NetworkClient networkClient) {
		this.networkClient = networkClient;

		currentWindow = new MainWindow(this, info);
		currentWindow.setVisible(true);
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
			int option = JOptionPane.showOptionDialog(currentWindow, panel, "Unlock KeyStore", JOptionPane.NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				try {
					keyStore = new ProtectedKeyStore(keystoreFile,
							new PasswordEncryption(SecurityUtils.secureHashChars(pass.getPassword())));
					keyStore.load();
					unlocked = true;
				} catch (IOException e) {
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

			int option = JOptionPane.showOptionDialog(currentWindow, panel, "New KeyStore", JOptionPane.NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
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

	@SuppressWarnings("unchecked")
	public <T> T getCurrentWindow(Class<T> clazz) {
		return (T) currentWindow;
	}

	public JFrame getCurrentWindow() {
		return currentWindow;
	}

	public ConnectionStore getConnectionStore() {
		return connectionStore;
	}

	public NetworkClient getNetworkClient() {
		return networkClient;
	}

	public static void main(String[] args) {
		INSTANCE = new SecureChatClientOld();
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
	}


}
