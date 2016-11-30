package com.securechat.client.connection;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JButton btnConnect;
	
	public LoginWindow(SecureChatClient client) {
		this.client = client;
		initialize();
	}

	 public void updateOptions(){
		 String[] names = client.getConnectionStore().getInfos().stream().map(r -> r.getServerName())
				 .toArray(size -> new String[size]);
//		ConnectionInfo[] infos = client.getConnectionStore().getInfos().toArray(new ConnectionInfo[0]);
		 connectionBox.setModel(new DefaultComboBoxModel(names));
		 btnConnect.setEnabled(names.length != 0);
	 }

	private void initialize() {
		frmSecureChat = new JFrame();
		frmSecureChat.setTitle("Secure Chat");
		frmSecureChat.setBounds(100, 100, 280, 225);
		frmSecureChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSecureChat.getContentPane().setLayout(null);

		JLabel lblSecureChat = new JLabel("Secure Chat");
		lblSecureChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblSecureChat.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblSecureChat.setBounds(10, 11, 233, 26);
		frmSecureChat.getContentPane().add(lblSecureChat);

		connectionBox= new JComboBox<String>();
		connectionBox.setBounds(83, 48, 160, 20);
		frmSecureChat.getContentPane().add(connectionBox);

		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(10, 51, 63, 14);
		frmSecureChat.getContentPane().add(lblConnection);

		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(false);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnConnect.setBounds(83, 126, 160, 23);
		frmSecureChat.getContentPane().add(btnConnect);

		JLabel lblServerName = new JLabel("Server Name");
		lblServerName.setBounds(10, 76, 63, 14);
		frmSecureChat.getContentPane().add(lblServerName);

		JLabel lblServerNameValue = new JLabel("");
		lblServerNameValue.setBounds(82, 76, 161, 14);
		frmSecureChat.getContentPane().add(lblServerNameValue);

		JLabel lblServerHost = new JLabel("Server Host");
		lblServerHost.setBounds(10, 101, 63, 14);
		frmSecureChat.getContentPane().add(lblServerHost);

		JLabel lblServerHostValue = new JLabel("");
		lblServerHostValue.setBounds(83, 101, 160, 14);
		frmSecureChat.getContentPane().add(lblServerHostValue);

		JMenuBar menuBar = new JMenuBar();
		frmSecureChat.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmSetupConnection = new JMenuItem("Setup Connection");
		mntmSetupConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetupConnectionDialog setup = new SetupConnectionDialog(frmSecureChat);
				setup.setVisible(true);
			}
		});
		mnFile.add(mntmSetupConnection);

		JMenuItem mntmOverrideConnection = new JMenuItem("Override Connection");
		mntmOverrideConnection.setEnabled(false);
		mnFile.add(mntmOverrideConnection);
	}

	public void open() {
		frmSecureChat.setVisible(true);
	}

	public JFrame getFrame() {
		return frmSecureChat;
	}
}
