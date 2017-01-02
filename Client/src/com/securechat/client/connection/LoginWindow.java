package com.securechat.client.connection;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Consumer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.securechat.client.SecureChatClient;
import com.securechat.client.network.NetworkClient;
import com.securechat.common.packets.ChallengePacket;
import com.securechat.common.packets.ChallengeResponsePacket;
import com.securechat.common.packets.ConnectPacket;
import com.securechat.common.packets.ConnectedPacket;

public class LoginWindow extends JFrame {
	private static final long serialVersionUID = -733929606760128273L;
	private SecureChatClient client;
	private JComboBox<String> connectionBox;
	private JLabel lblServerNameValue, lblServerHostValue;
	private JButton btnConnect;
	private ConnectionInfo[] infos;

	public LoginWindow(SecureChatClient client) {
		this.client = client;
		setResizable(false);
		setTitle("Secure Chat");
		setBounds(100, 100, 265, 210);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblSecureChat = new JLabel("Secure Chat");
		lblSecureChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblSecureChat.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblSecureChat.setBounds(10, 11, 233, 26);
		getContentPane().add(lblSecureChat);

		connectionBox = new JComboBox<String>();
		connectionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					updateSelection();
				}
			}
		});
		connectionBox.setBounds(83, 48, 160, 20);
		getContentPane().add(connectionBox);

		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(10, 51, 63, 14);
		getContentPane().add(lblConnection);

		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(false);
		btnConnect.addActionListener(this::connect);
		btnConnect.setBounds(83, 126, 160, 23);
		getContentPane().add(btnConnect);

		JLabel lblServerName = new JLabel("Server Name");
		lblServerName.setBounds(10, 76, 63, 14);
		getContentPane().add(lblServerName);

		lblServerNameValue = new JLabel("");
		lblServerNameValue.setBounds(82, 76, 161, 14);
		getContentPane().add(lblServerNameValue);

		JLabel lblServerHost = new JLabel("Server Host");
		lblServerHost.setBounds(10, 101, 63, 14);
		getContentPane().add(lblServerHost);

		lblServerHostValue = new JLabel("");
		lblServerHostValue.setBounds(83, 101, 160, 14);
		getContentPane().add(lblServerHostValue);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmSetupConnection = new JMenuItem("Setup Connection");
		mntmSetupConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetupConnectionDialog setup = new SetupConnectionDialog(client);
				setup.setVisible(true);
			}
		});
		mnFile.add(mntmSetupConnection);

		JMenuItem mntmOverrideConnection = new JMenuItem("Override Connection");
		mntmOverrideConnection.setEnabled(false);
		mnFile.add(mntmOverrideConnection);
	}

	public void updateOptions() {
		infos = client.getConnectionStore().getInfos().toArray(new ConnectionInfo[0]);
		String[] names = new String[infos.length];
		for (int i = 0; i < infos.length; i++) {
			ConnectionInfo info = infos[i];
			names[i] = info.getServerName() + "(" + info.getUsername() + ")";
		}
		connectionBox.setModel(new DefaultComboBoxModel<String>(names));
		updateSelection();
	}

	private void updateSelection() {
		if (infos.length == 0) {
			lblServerNameValue.setText("");
			lblServerHostValue.setText("");
			btnConnect.setEnabled(false);
		}
		int index = connectionBox.getSelectedIndex();
		ConnectionInfo info = infos[index];

		lblServerNameValue.setText(info.getServerName() + " (" + info.getUsername() + ")");
		lblServerHostValue.setText(info.getServerIp() + ":" + info.getServerPort());
		btnConnect.setEnabled(true);
	}

	private ConnectingDialog connectingDialog;
	private Thread connectingThread;
	private String connectingTask;

	private void connect(ActionEvent event) {
		ConnectionInfo info = infos[connectionBox.getSelectedIndex()];
		connectingDialog = new ConnectingDialog(client, info);
		SwingUtilities.invokeLater(() -> connectingDialog.setVisible(true));

		connectingThread = new Thread(() -> {
			connectingDialog.setStatus("Connecting to " + info.getServerIp() + ":" + info.getServerPort() + "...");

			Consumer<String> disconnectHandler = r -> {
				JOptionPane.showMessageDialog(connectingDialog, "Reason: " + r + " when " + connectingTask + ".",
						"Failed to connect to the server", JOptionPane.ERROR_MESSAGE);
				connectingDialog.dispose();
				connectingThread.stop();
			};

			NetworkClient networkClient = new NetworkClient();
			try {
				connectingTask = "connecting";
				networkClient.connect(info.getServerIp(), info.getServerPort(), info.getPublicKey(),
						info.getPrivateKey(), disconnectHandler);
			} catch (Exception e) {
				e.printStackTrace();
				disconnectHandler.accept("Internal Error (" + e.getMessage() + ")");
				return;
			}

			Consumer<ConnectedPacket> responseHandler = c -> {
				JOptionPane.showMessageDialog(connectingDialog, "Connected!", "Connected to the server!!",
						JOptionPane.ERROR_MESSAGE);
				connectingTask = "connected";
				setVisible(false);
				client.onConnected(info, networkClient);
			};

			Consumer<ChallengePacket> challengeHandler = c -> {
				connectingDialog.setStatus("Completing challenge...");
				connectingTask = "completing the challenge";
				networkClient.setSingleHandler(ConnectedPacket.class, responseHandler);
				networkClient.sendPacket(new ChallengeResponsePacket(c.getTempCode()));
			};

			connectingDialog.setStatus("Logging in...");
			connectingTask = "logging in";
			networkClient.setSingleHandler(ChallengePacket.class, challengeHandler);
			networkClient.sendPacket(new ConnectPacket(info.getUsername(), info.getCode()));
		});
		connectingThread.start();
	}

}
