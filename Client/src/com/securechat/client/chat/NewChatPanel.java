package com.securechat.client.chat;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class NewChatPanel extends JPanel {
	private JTextField textField;

	public NewChatPanel() {
		Dimension expectedDimension = new Dimension(374, 179);
		setPreferredSize(expectedDimension);
		setMaximumSize(expectedDimension);
		setMinimumSize(expectedDimension);
		setLayout(null);
		
		JLabel lblNewChat = new JLabel("New Chat");
		lblNewChat.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewChat.setBounds(10, 11, 153, 27);
		add(lblNewChat);
		
		JLabel lblNewLabel = new JLabel("<html>The other user will have to accept your request before you can message them.</html>");
		lblNewLabel.setBounds(10, 49, 354, 27);
		add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 90, 75, 14);
		add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(95, 87, 269, 20);
		add(textField);
		textField.setColumns(10);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Allow offline messages");
		chckbxNewCheckBox.setBounds(95, 114, 228, 23);
		add(chckbxNewCheckBox);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(95, 144, 75, 23);
		add(btnCreate);
		
		JLabel lblAllowOfflineMessages = new JLabel("Advanced");
		lblAllowOfflineMessages.setBounds(10, 118, 75, 14);
		add(lblAllowOfflineMessages);
		
		JButton button = new JButton("?");
		button.setBounds(327, 114, 37, 23);
		add(button);

	}
}
