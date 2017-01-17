package com.securechat.client.chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.securechat.client.SecureChatClient;
import com.securechat.client.connection.ConnectionInfo;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -4395256970704686192L;
	private JPanel contentPane, contactsPanel;
	private JSplitPane mainSplitPane;

	public MainWindow(SecureChatClient client, ConnectionInfo info) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 729, 498);
		setTitle(info.getUsername()+"@"+info.getServerName()+" - Secure Chat Client");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		mainSplitPane = new JSplitPane();
		mainSplitPane.setContinuousLayout(true);
		contentPane.add(mainSplitPane, BorderLayout.CENTER);

		JScrollPane contactsScrollPane = new JScrollPane();
		contactsScrollPane.setSize(new Dimension(220, 300));
		contactsScrollPane.setMinimumSize(new Dimension(220, 300));
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
		contactsScrollPane.setColumnHeaderView(contactsTopPanel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contactsTopPanel.add(panel);
		
				JLabel lblNew = new JLabel("New Chat");
				lblNew.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						addChat("CSNewman");
					}
				});
				panel.add(lblNew);
				
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
				contactsTopPanel.add(panel_1);
				
				JLabel lblNewLabel = new JLabel("New Group");
				panel_1.add(lblNewLabel);
				
				JPanel panel_2 = new JPanel();
				panel_2.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
				contactsTopPanel.add(panel_2);
				
				JLabel lblPending = new JLabel("Pending");
				panel_2.add(lblPending);

//		final JPopupMenu newPopup = new JPopupMenu();
//		newPopup.add(new JMenuItem(new FunctionalAction("Chat", e -> {
////			NewChatDialog dialog = new NewChatDialog(client);
////			dialog.setVisible(true);
//			
//		})));
//
//		newPopup.add(new JMenuItem(new AbstractAction("Group") {
//			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(MainWindow.this, "Option 2 selected");
//			}
//		}));
//
//		contactsTopPanel.addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				newPopup.show(e.getComponent(), e.getX(), e.getY());
//			}
//		});
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		box.add(Box.createVerticalGlue());
		box.add(new NewChatPanel());
		box.add(Box.createVerticalGlue());
//		add(box);

//		mainSplitPane.setRightComponent(box);
		
		mainSplitPane.setRightComponent(new PendingPanel());

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
//		JPanel spanel = new JPanel();
//		spanel.add(new JLabel("<html><b>" + name + "<b></html>"));
//		spanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridwidth = GridBagConstraints.REMAINDER;
		gbc1.weightx = 1;
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		contactsPanel.add(new ChatNamePanel(), gbc1, 0);

		validate();
		repaint();
	}

}
