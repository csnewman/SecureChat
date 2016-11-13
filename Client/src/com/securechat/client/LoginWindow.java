package com.securechat.client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class LoginWindow {

	private JFrame frmSecureChat;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					LoginWindow window = new LoginWindow();
					window.frmSecureChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
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

		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(83, 48, 160, 20);
		frmSecureChat.getContentPane().add(comboBox);

		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(10, 51, 63, 14);
		frmSecureChat.getContentPane().add(lblConnection);

		JButton btnConnect = new JButton("Connect");
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
				System.out.println("Setup Clicked");
			}
		});
		mnFile.add(mntmSetupConnection);

		JMenuItem mntmOverrideConnection = new JMenuItem("Override Connection");
		mnFile.add(mntmOverrideConnection);
	}
}
