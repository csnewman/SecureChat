package com.securechat.client.chat;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import javax.swing.border.EtchedBorder;

public class ChatNamePanel extends JPanel {
	private static final long serialVersionUID = 3372757647378845676L;

	public ChatNamePanel() {
		setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		
		JLabel label = new JLabel("<html><b>hello<b></html>");
		panel.add(label);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.EAST);
		
		JLabel label_1 = new JLabel("123");
		panel_1.add(label_1);
	}

}
