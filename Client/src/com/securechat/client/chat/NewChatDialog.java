package com.securechat.client.chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.securechat.client.SecureChatClient;
import com.securechat.common.packets.NewChatPacket;

public class NewChatDialog extends JDialog {
	private static final long serialVersionUID = 5648635583143452660L;
	private final JPanel contentPanel = new JPanel();
	private SecureChatClient client;
	private JTextField tfUsername;
	private JButton btnCreate;
	
	public NewChatDialog(SecureChatClient client) {
		super(client.getCurrentWindow(), "New chat", true);
		this.client = client;
		setResizable(false);
		setBounds(100, 100, 277, 124);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblInfo = new JLabel("The other person will need to accept your chat invite");
		lblInfo.setBounds(10, 11, 253, 14);
		contentPanel.add(lblInfo);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 36, 62, 14);
		contentPanel.add(lblUsername);
		
		tfUsername = new JTextField();
		tfUsername.setBounds(82, 33, 181, 20);
		contentPanel.add(tfUsername);
		tfUsername.setColumns(10);
		
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(this::create);
		btnCreate.setBounds(82, 64, 179, 23);
		contentPanel.add(btnCreate);
	}
	
	private void create(ActionEvent e){
		btnCreate.setEnabled(false);
		tfUsername.setEnabled(false);
		client.getNetworkClient().sendPacket(new NewChatPacket(tfUsername.getText()));
		
//		JOptionPane.showMessageDialog(this, "User does not exist!", "Failed to create", JOptionPane.ERROR_MESSAGE);
		
	}
}
