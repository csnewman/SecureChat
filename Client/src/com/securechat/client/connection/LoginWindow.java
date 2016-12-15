package com.securechat.client.connection;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import com.securechat.client.SecureChatClient;

public class LoginWindow {
	private SecureChatClient client;
	private JFrame frmSecureChat;
	private JComboBox<String> connectionBox;
	private JLabel lblServerNameValue, lblServerHostValue;
	private JButton btnConnect;
	private ConnectionInfo[] infos;

	public LoginWindow(SecureChatClient client) {
		this.client = client;
		initialize();
	}

	private void initialize() {
		frmSecureChat = new JFrame();
		frmSecureChat.setResizable(false);
		frmSecureChat.setTitle("Secure Chat");
		frmSecureChat.setBounds(100, 100, 265, 210);
		frmSecureChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSecureChat.getContentPane().setLayout(null);

		JLabel lblSecureChat = new JLabel("Secure Chat");
		lblSecureChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblSecureChat.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblSecureChat.setBounds(10, 11, 233, 26);
		frmSecureChat.getContentPane().add(lblSecureChat);

		connectionBox = new JComboBox<String>();
		connectionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					updateSelection();
				}
			}
		});
		connectionBox.setBounds(83, 48, 160, 20);
		frmSecureChat.getContentPane().add(connectionBox);

		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(10, 51, 63, 14);
		frmSecureChat.getContentPane().add(lblConnection);

		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(false);
		btnConnect.addActionListener(this::connect);
		btnConnect.setBounds(83, 126, 160, 23);
		frmSecureChat.getContentPane().add(btnConnect);

		JLabel lblServerName = new JLabel("Server Name");
		lblServerName.setBounds(10, 76, 63, 14);
		frmSecureChat.getContentPane().add(lblServerName);

		lblServerNameValue = new JLabel("");
		lblServerNameValue.setBounds(82, 76, 161, 14);
		frmSecureChat.getContentPane().add(lblServerNameValue);

		JLabel lblServerHost = new JLabel("Server Host");
		lblServerHost.setBounds(10, 101, 63, 14);
		frmSecureChat.getContentPane().add(lblServerHost);

		lblServerHostValue = new JLabel("");
		lblServerHostValue.setBounds(83, 101, 160, 14);
		frmSecureChat.getContentPane().add(lblServerHostValue);

		JMenuBar menuBar = new JMenuBar();
		frmSecureChat.setJMenuBar(menuBar);

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

	private void connect(ActionEvent e) {
		client.connect(infos[connectionBox.getSelectedIndex()]);
	}

	public void open() {
		frmSecureChat.setVisible(true);
	}

	public JFrame getFrame() {
		return frmSecureChat;
	}
}
