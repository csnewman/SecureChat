package com.securechat.client.connection;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.JList;
import javax.swing.JFormattedTextField;
import java.awt.ScrollPane;
import javax.swing.JTextPane;
import java.awt.TextArea;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class SetupConnectionDialog extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SetupConnectionDialog frame = new SetupConnectionDialog();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SetupConnectionDialog() {
		setTitle("New Connection");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 444, 280);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblConnectionInfo = new JLabel("Connection Info File");
		lblConnectionInfo.setBounds(10, 45, 112, 14);
		contentPane.add(lblConnectionInfo);
		
		textField = new JTextField();
		textField.setBounds(132, 42, 235, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("...");
		button.setBounds(380, 41, 38, 23);
		contentPane.add(button);
		
		JLabel lblASecureConnection = new JLabel("A secure connection info file is required. This can be found in the server folder.");
		lblASecureConnection.setBounds(10, 20, 408, 14);
		contentPane.add(lblASecureConnection);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 70, 112, 14);
		contentPane.add(lblPassword);
		
		textField_1 = new JPasswordField();
		textField_1.setBounds(132, 67, 235, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnDecrypt = new JButton("Decrypt");
		btnDecrypt.setBounds(132, 98, 112, 23);
		contentPane.add(btnDecrypt);
		
		JButton btnImport = new JButton("Import");
		btnImport.setEnabled(false);
		btnImport.setBounds(255, 98, 112, 23);
		contentPane.add(btnImport);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 132, 408, 98);
		contentPane.add(scrollPane);
		
		JTextArea txtrExampleExampleExample = new JTextArea();
		scrollPane.setViewportView(txtrExampleExampleExample);
		txtrExampleExampleExample.setText("Ready...");
		txtrExampleExampleExample.setLineWrap(true);
		txtrExampleExampleExample.setEditable(false);
	}
}
