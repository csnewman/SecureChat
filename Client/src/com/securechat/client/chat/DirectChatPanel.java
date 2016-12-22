package com.securechat.client.chat;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;

public class DirectChatPanel extends JPanel {
	private JPanel messageList;

	/**
	 * Create the panel.
	 */
	public DirectChatPanel() {
		
		setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_1 = new JButton("Send");
		panel_2.add(btnNewButton_1, BorderLayout.EAST);

		JTextPane textPane = new JTextPane();
		panel_2.add(textPane, BorderLayout.CENTER);

		JPanel panel_3 = new JPanel();
		add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("[USERNAME HERE]");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_3.add(lblNewLabel, BorderLayout.WEST);

		JLabel lblOptions = new JLabel("Options");
		panel_3.add(lblOptions, BorderLayout.EAST);

		final JPopupMenu popup = new JPopupMenu();
		popup.add(new JMenuItem(new AbstractAction("Option 1") {
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(MainWindow.this, "Option 1 selected");
			}
		}));
		popup.add(new JMenuItem(new AbstractAction("Option 2") {
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(MainWindow.this, "Option 2 selected");
			}
		}));

		lblOptions.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new CompoundBorder());
		add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));

		messageList = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		messageList.add(new JPanel().add(new JLabel("Start of conversation with [USERNAME]")), gbc);

		
		JScrollPane scrollPane_1 = new JScrollPane(messageList);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_4.add(scrollPane_1, BorderLayout.CENTER);
		
	}

}
