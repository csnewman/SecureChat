package com.securechat.client;

import java.awt.EventQueue;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.securechat.client.connection.ConnectingDialog;
import com.securechat.client.connection.ConnectionInfo;
import com.securechat.client.connection.ConnectionStore;
import com.securechat.client.connection.LoginWindow;
import com.securechat.client.network.NetworkClient;
import com.securechat.common.packets.ChallengePacket;
import com.securechat.common.packets.ChallengeResponsePacket;
import com.securechat.common.packets.ConnectPacket;
import com.securechat.common.packets.ConnectedPacket;
import com.securechat.common.packets.DisconnectPacket;
import com.securechat.common.packets.IPacket;
import com.securechat.common.security.PasswordEncryption;
import com.securechat.common.security.ProtectedKeyStore;
import com.securechat.common.security.SecurityUtils;

public class SecureChatClient {
	private static final File keystoreFile = new File("keystore.bin");
	private static SecureChatClient INSTANCE;
	private JFrame currentWindow;
	private ProtectedKeyStore keyStore;
	private LoginWindow loginWindow;
	private ConnectionStore connectionStore;
	private NetworkClient networkClient;
	private ConnectingDialog connectingDialog;

	public void init() {
		loginWindow = new LoginWindow(INSTANCE);
		currentWindow = loginWindow.getFrame();
		loginWindow.open();

		if (keystoreFile.exists()) {
			unlockKeyStore();
		} else {
			generateKeyStore();
		}
		connectionStore = new ConnectionStore(keyStore.getOrGenKeyPair("connections"));
		connectionStore.tryLoadAndSave();

		keyStore.save();

		loginWindow.updateOptions();

	}

	private Thread connectingThread;
	public void connect(ConnectionInfo info) {
		connectingDialog = new ConnectingDialog(this, info);
		SwingUtilities.invokeLater(() -> connectingDialog.setVisible(true));

		connectingThread = new Thread(() -> {
			connectingDialog.setStatus("Connecting to " + info.getServerIp() + ":" + info.getServerPort() + "...");

			Consumer<String> disconnectHandler = r -> {
				JOptionPane.showMessageDialog(connectingDialog, "Reason: " + r, "Failed to connect to the server", 
						JOptionPane.ERROR_MESSAGE);
				connectingDialog.dispose();
				connectingThread.stop();
			};

			networkClient = new NetworkClient();
			try {
				networkClient.connect(info.getServerIp(), info.getServerPort(), info.getPublicKey(),
						info.getPrivateKey(), disconnectHandler);
			} catch (Exception e) {
				e.printStackTrace();
				disconnectHandler.accept("Internal Error (" + e.getMessage() + ")");
				return;
			}

			// currentWindow = new MainWindow(this);
			// currentWindow.setVisible(true);

			Consumer<ConnectedPacket> responseHandler = c -> {
				JOptionPane.showMessageDialog(connectingDialog, "Connected!", "Connected to the server!!",
						JOptionPane.ERROR_MESSAGE);
			};

			Consumer<ChallengePacket> challengeHandler = c -> {
				connectingDialog.setStatus("Completing challenge...");
				networkClient.setSingleHandler(ConnectedPacket.class, responseHandler);
				networkClient.sendPacket(new ChallengeResponsePacket(c.getTempCode()));
			};

			connectingDialog.setStatus("Logging in...");
			networkClient.setSingleHandler(ChallengePacket.class, challengeHandler);
			networkClient.sendPacket(new ConnectPacket(info.getUsername(), info.getCode()));
		});
		connectingThread.start();
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

	public LoginWindow getLoginWindow() {
		return loginWindow;
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
	}

}
