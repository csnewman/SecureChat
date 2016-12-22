package com.securechat.client.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;

import com.securechat.client.SecureChatClient;
import com.securechat.client.util.FunctionalAction;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -4395256970704686192L;
	private JPanel contentPane, contactsPanel;
	private JSplitPane mainSplitPane;

	public MainWindow(SecureChatClient client) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		mainSplitPane = new JSplitPane();
		mainSplitPane.setContinuousLayout(true);
		contentPane.add(mainSplitPane, BorderLayout.CENTER);

		JScrollPane contactsScrollPane = new JScrollPane();
		contactsScrollPane.setSize(new Dimension(200, 300));
		contactsScrollPane.setMinimumSize(new Dimension(200, 300));
		mainSplitPane.setLeftComponent(contactsScrollPane);

		contactsPanel = new JPanel();
		contactsScrollPane.setViewportView(contactsPanel);
		contactsPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		contactsPanel.add(new JPanel(), gbc);

		JPanel contactsTopPanel = new JPanel();
		contactsTopPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contactsScrollPane.setColumnHeaderView(contactsTopPanel);

		JLabel lblNew = new JLabel("New");
		contactsTopPanel.add(lblNew);

		final JPopupMenu newPopup = new JPopupMenu();
		newPopup.add(new JMenuItem(new FunctionalAction("Chat", e -> {
			NewChatDialog dialog = new NewChatDialog(client);
			dialog.setVisible(true);
			addChat("CSNewman");
		})));

		newPopup.add(new JMenuItem(new AbstractAction("Group") {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainWindow.this, "Option 2 selected");
			}
		}));

		contactsTopPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				newPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		mainSplitPane.setRightComponent(new NoChatPanel());

		// JTextPane textPane = new JTextPane();
		// textPane.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyTyped(KeyEvent arg0) {
		// System.out.println(textPane.getText());
		// }
		// });
		// textPane.setContentType("text/html");
		// splitPane.setRightComponent(textPane);

		// JLabel lblNewLabel = new JLabel("<html><font
		// color=#ffffdd>Hello</font> <b> World <b></html>");
		// splitPane.setRightComponent(lblNewLabel);

		// JButton btnNewButton_1 = new JButton("New button");
		// btnNewButton_1.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// JPanel spanel = new JPanel();
		// spanel.add(new JLabel("Hello"));
		// spanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
		// GridBagConstraints gbc = new GridBagConstraints();
		// gbc.gridwidth = GridBagConstraints.REMAINDER;
		// gbc.weightx = 1;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// panel.add(spanel, gbc, 0);
		//
		// validate();
		// repaint();
		// }
		// });
		// splitPane.setRightComponent(btnNewButton_1);

	}

	public void addChat(String name) {
		JPanel spanel = new JPanel();
		spanel.add(new JLabel("<html><b>" + name + "<b></html>"));
		spanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridwidth = GridBagConstraints.REMAINDER;
		gbc1.weightx = 1;
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		contactsPanel.add(spanel, gbc1, 0);

		validate();
		repaint();
	}

}
