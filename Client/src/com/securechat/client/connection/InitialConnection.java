package com.securechat.client.connection;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.KeyPair;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.securechat.basicencryption.RSAEncryption;
import com.securechat.client.SecureChatClient;
import com.securechat.client.network.NetworkClient;
import com.securechat.common.packets.RegisterPacket;
import com.securechat.common.packets.RegisterResponsePacket;

public class InitialConnection extends JDialog {
	private static final long serialVersionUID = 3290081829877215800L;
	private SecureChatClient client;
	private ConnectionInfo connectionInfo;
	private JPanel contentPane;
	private JTextField usernameField;
	private JLabel lblStatusValue;
	private JButton btnCreateAccount;

	public InitialConnection(SecureChatClient client, ConnectionInfo connectionInfo) {
		super(client.getCurrentWindow(), "Initial Connection", true);
		this.client = client;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				cancelCreation();
			}
		});
		this.connectionInfo = connectionInfo;
		setBounds(100, 100, 286, 165);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 39, 70, 14);
		contentPane.add(lblUsername);

		usernameField = new JTextField();
		usernameField.setBounds(90, 36, 167, 20);
		contentPane.add(usernameField);
		usernameField.setColumns(10);

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(10, 64, 70, 14);
		contentPane.add(lblStatus);

		lblStatusValue = new JLabel("Ready");
		lblStatusValue.setBounds(90, 64, 167, 14);
		contentPane.add(lblStatusValue);

		JLabel lblInfo = new JLabel("Please choose a username");
		lblInfo.setBounds(10, 11, 247, 14);
		contentPane.add(lblInfo);

		btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(this::createAccount);
		btnCreateAccount.setBounds(90, 89, 167, 23);
		contentPane.add(btnCreateAccount);
	}

	private boolean creationInProgress;
	private Thread creationThread;

	private void cancelCreation() {
		if (creationInProgress) {
			// creationThread.
		}
	}

	private void createAccount(ActionEvent e) {
		if (usernameField.getText().length() == 0) {
			setStatus(false, "Username too short");
			return;
		}
		setStatus(true, "Starting...");
		creationThread = new Thread(this::doCreation);
		creationThread.start();
	}

	private void doCreation() {
		String username = usernameField.getText();

		NetworkClient client;
		try {
			setStatus(true, "Generating key pair");
			KeyPair pair = RSAEncryption.generateKeyPair();

			setStatus(true, "Connecting to server");
			client = new NetworkClient();

			Consumer<String> disconnectHandler = r -> {
				setStatus(false, "Failed: " + r);
			};

			client.connect(connectionInfo.getServerIp(), connectionInfo.getServerPort(), connectionInfo.getPublicKey(),
					pair.getPrivate(), disconnectHandler);

			client.setSingleHandler(RegisterResponsePacket.class, r -> {
				switch (r.getStatus()) {
				case Success:
					setStatus(true, "Saving");
					client.disconnect();

					connectionInfo.complete(username, pair.getPrivate(), r.getCode());
					this.client.getConnectionStore().addConnection(connectionInfo);
					this.client.getCurrentWindow(LoginWindow.class).updateOptions();

					setStatus(true, "Success");
					JOptionPane.showMessageDialog(this,
							"Your account has been registered with the server.\nYou may now connect to the server.",
							"Success", JOptionPane.INFORMATION_MESSAGE);
					dispose();

					break;
				case UsernameTaken:
					setStatus(false, "Username taken");
					break;
				default:
					break;
				}
			});

			setStatus(true, "Registering username");
			client.sendPacket(new RegisterPacket(username, pair.getPublic()));
		} catch (Exception e) {
			e.printStackTrace();
			setStatus(false, "Failed to connect");
		}
	}

	private void setStatus(boolean inProgress, String text) {
		btnCreateAccount.setEnabled(!inProgress);
		usernameField.setEditable(!inProgress);
		lblStatusValue.setText(text);
		creationInProgress = inProgress;
	}

}
