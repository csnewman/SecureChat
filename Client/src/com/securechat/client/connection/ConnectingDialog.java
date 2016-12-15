package com.securechat.client.connection;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.securechat.client.SecureChatClient;

public class ConnectingDialog extends JDialog {
	private static final long serialVersionUID = 6865145058967191531L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblStatusValue;

	public ConnectingDialog(SecureChatClient client, ConnectionInfo info) {
		super(client.getCurrentWindow(), "Connecting to the server...", true);
		setResizable(false);
		setBounds(100, 100, 450, 117);
		setLocationRelativeTo(client.getCurrentWindow());
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblInfo = new JLabel("Connecting to " + info.getServerName() + " as " + info.getUsername());
		lblInfo.setBounds(10, 11, 424, 14);
		contentPanel.add(lblInfo);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(10, 61, 424, 14);
		contentPanel.add(progressBar);

		lblStatusValue = new JLabel("Waiting...");
		lblStatusValue.setBounds(10, 36, 424, 14);
		contentPanel.add(lblStatusValue);
	}

	public void setStatus(String text) {
		lblStatusValue.setText(text);
	}

}
