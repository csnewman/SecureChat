package com.securechat.plugins.basicgui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.securechat.common.gui.IKeystoreGui;
import com.securechat.common.security.IKeystore;

public class KeystoreGui implements IKeystoreGui {
	private BasicGuiPlugin plugin;

	public KeystoreGui(BasicGuiPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void show(IKeystore keystore) {
		if (keystore.isLoaded()) {
			throw new RuntimeException("Keystore is already loaded");
		}

		if (keystore.exists()) {
			while (true) {
				JPanel panel = new JPanel();
				JLabel label = new JLabel("Keystore Password:");
				panel.add(label);
				JPasswordField pass = new JPasswordField(20);
				panel.add(pass);

				String[] options = new String[] { "Unlock", "Quit" };
				int option = JOptionPane.showOptionDialog(plugin.getCurrentWindow(), panel, "Unlock KeyStore",
						JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == 0) {
					if (keystore.load(pass.getPassword())) {
						return;
					}
				} else {
					System.exit(-1);
				}
			}
		} else {
			while (true) {
				JPanel panel = new JPanel();
				JLabel label = new JLabel("New Keystore Password:");
				panel.add(label);
				JPasswordField pass = new JPasswordField(20);
				panel.add(pass);

				String[] options = new String[] { "Lock", "Quit" };

				int option = JOptionPane.showOptionDialog(plugin.getCurrentWindow(), panel, "New KeyStore",
						JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == 0) {
					char[] password = pass.getPassword();
					if (password.length == 0) {
						continue;
					}
					keystore.generate(password);

				} else {
					System.exit(-1);
				}
			}
		}
	}

	@Override
	public String getImplName() {
		return "official-gui_keystore";
	}

}
