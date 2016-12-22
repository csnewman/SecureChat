package com.securechat.client.chat;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class NoChatPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public NoChatPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblNoChatOpen = new JLabel("No chat open");
		lblNoChatOpen.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNoChatOpen, BorderLayout.CENTER);

	}

}
