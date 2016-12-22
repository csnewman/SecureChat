package com.securechat.client.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.Utilities;

public class MessagePanel extends JPanel {
	static ImageIcon SMILE_IMG = createImage();

	static ImageIcon createImage() {
		BufferedImage res = new BufferedImage(17, 17, BufferedImage.TYPE_INT_ARGB);
		Graphics g = res.getGraphics();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.yellow);
		g.fillOval(0, 0, 16, 16);

		g.setColor(Color.black);
		g.drawOval(0, 0, 16, 16);

		g.drawLine(4, 5, 6, 5);
		g.drawLine(4, 6, 6, 6);

		g.drawLine(11, 5, 9, 5);
		g.drawLine(11, 6, 9, 6);

		g.drawLine(4, 10, 8, 12);
		g.drawLine(8, 12, 12, 10);
		g.dispose();

		return new ImageIcon(res);
	}

	/**
	 * Create the panel.
	 */
	public MessagePanel() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.EAST);

		JLabel lblNewLabel_1 = new JLabel("Sending");
		panel_1.add(lblNewLabel_1);

		JLabel label = new JLabel("12:11 21/12/2016");
		panel_1.add(label);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);

		JLabel label_1 = new JLabel("You");
		panel_2.add(label_1);

		JPanel panel_3 = new JPanel();
		add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));

		JTextPane txtpnHelloHi = new JTextPane();
		txtpnHelloHi.setEditorKit(new StyledEditorKit());
		txtpnHelloHi.setBackground(new Color(240, 240, 240, 255));
		txtpnHelloHi.setContentType("text/html");
		txtpnHelloHi.setText("<html>\r\n <body><h1>My First Heading</h1><p>My first paragraph. </p></body></html>");
		txtpnHelloHi.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent event) {
				final DocumentEvent e = event;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (e.getDocument() instanceof StyledDocument) {
							try {
								StyledDocument doc = (StyledDocument) e.getDocument();
								int start = Utilities.getRowStart(txtpnHelloHi, Math.max(0, e.getOffset() - 1));
								int end = Utilities.getWordStart(txtpnHelloHi, e.getOffset() + e.getLength());
								String text = doc.getText(start, end - start);

								int i = text.indexOf(":)");
								while (i >= 0) {
									final SimpleAttributeSet attrs = new SimpleAttributeSet(
											doc.getCharacterElement(start + i).getAttributes());
									if (StyleConstants.getIcon(attrs) == null) {
										StyleConstants.setIcon(attrs, SMILE_IMG);
										doc.remove(start + i, 2);
										doc.insertString(start + i, ":)", attrs);
									}
									i = text.indexOf(":)", i + 2);
								}
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}
						}
					}
				});
			}

			public void removeUpdate(DocumentEvent e) {
			}

			public void changedUpdate(DocumentEvent e) {
			}
		});

		panel_3.add(txtpnHelloHi, BorderLayout.CENTER);

	}

}
