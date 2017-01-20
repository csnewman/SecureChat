package com.securechat.plugins.basicgui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

public class KeystoreDialog extends JDialog {
	private static final long serialVersionUID = 4924137066967064300L;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordField;
	private boolean completed;
	
	public KeystoreDialog(boolean generate, String msg) {
		super((Frame) null, Dialog.ModalityType.TOOLKIT_MODAL);
		completed = false;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 278, 146);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblPleaseEnterYour = new JLabel(generate ? "Please enter a password for your new keystore"
				: "Please enter your keystore password to unlock it");
		lblPleaseEnterYour.setBounds(10, 11, 250, 14);
		contentPanel.add(lblPleaseEnterYour);

		passwordField = new JPasswordField();
		passwordField.setBounds(10, 36, 250, 20);
		contentPanel.add(passwordField);

		JLabel lblNewLabel = new JLabel(msg != null ? msg : "Waiting...");
		lblNewLabel.setBounds(10, 67, 250, 14);
		contentPanel.add(lblNewLabel);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton(generate ? "Set" : "Unlock");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		buttonPane.add(cancelButton);
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				completed = true;
				setVisible(false);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}

	public boolean didComplete() {
		return completed;
	}
	
	public char[] getPassword(){
		return passwordField.getPassword();
	}
	
}
