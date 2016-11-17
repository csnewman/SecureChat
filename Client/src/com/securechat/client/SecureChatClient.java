package com.securechat.client;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import com.securechat.common.security.ProtectedKeyStore;

public class SecureChatClient {
	private static SecureChatClient INSTANCE;
	private ProtectedKeyStore keyStore;
	private LoginWindow loginWindow;

	public void init() {
		LoginWindow window = new LoginWindow(INSTANCE);
		window.open();
		
		File keystoreFile = new File("keystore.blob");
		if(keystoreFile.exists()){
			unlockKeyStore();
		}else{
			
		}
		
		// keyStore = new ProtectedKeyStore(new File("keystore.blob"),
		// passwordHash)

	}

	private void unlockKeyStore() {
		boolean unlocked = false;
		while(!unlocked){
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Keystore Password:");
			panel.add(label);
			JPasswordField pass = new JPasswordField(20);
			panel.add(pass);
			
			String[] options = new String[] { "Unlock", "Quit" };
			int option = JOptionPane.showOptionDialog(loginWindow.getFrame(), panel, "Unlock KeyStore",
					JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				String password = new String(pass.getPassword());
				
				
				
			} else {
				System.exit(-1);
			}			
		}
	}
	
	private void generateKeyStore(){
		
	}

	public static void main(String[] args) {
		INSTANCE = new SecureChatClient();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					INSTANCE.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// KeyPair pair = SecurityUtils.generateKeyPair();
		//
		// System.out.println("Public: " +
		// Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
		// System.out.println("Private: " + new
		// String(pair.getPrivate().getEncoded()));
		//
		//
		// long start = System.currentTimeMillis();
		// System.out.println("Encrypted: " + new
		// String(SecurityUtils.encryptData("Hello".getBytes(),
		// pair.getPublic())));
		// System.out.println(System.currentTimeMillis() - start);
		//// SecurityUtils.hashData(input)

		// new NetworkClient().connect("127.0.0.1", 1234);

	}

}
