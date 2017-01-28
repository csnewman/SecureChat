package com.securechat.plugins.basicgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.network.IConnectionProfile;
import com.securechat.api.common.network.IConnectionProfileProvider;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IPasswordEncryption;
import com.securechat.api.common.storage.IStorage;

public class SetupConnectionDialog extends JDialog {
	private static final long serialVersionUID = 2659950995253670666L;
	private BasicGuiPlugin plugin;
	private JPanel contentPane;
	private JTextField fileField;
	private JPasswordField passwordField;
	private JLabel lblNameValue, lblStatusValue, lblIPValue, lblPortValue;
	private JButton btnImport;
	
	@InjectInstance
	private IConnectionProfileProvider profileProvider;
	@InjectInstance
	private IStorage storage;
	@InjectInstance
	private IImplementationFactory factory;
	private IConnectionProfile profile;

	public SetupConnectionDialog(BasicGuiPlugin plugin) {
		super(plugin.getCurrentWindow(), "New Connection", true);
		this.plugin = plugin;
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 436, 271);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblConnectionInfo = new JLabel("Connection Info File");
		lblConnectionInfo.setBounds(10, 45, 112, 14);
		contentPane.add(lblConnectionInfo);

		fileField = new JTextField();
		fileField.setBounds(132, 42, 235, 20);
		contentPane.add(fileField);
		fileField.setColumns(10);

		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Secure Chat Connection Infos (.scci)",
						"scci");
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File(fileField.getText().length() == 0 ? "." : fileField.getText()));
				int returnVal = chooser.showOpenDialog(SetupConnectionDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		button.setBounds(380, 41, 38, 23);
		contentPane.add(button);

		JLabel lblASecureConnection = new JLabel(
				"A secure connection info file is required. This can be found in the server folder.");
		lblASecureConnection.setBounds(10, 20, 408, 14);
		contentPane.add(lblASecureConnection);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 70, 112, 14);
		contentPane.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(132, 67, 235, 20);
		contentPane.add(passwordField);
		passwordField.setColumns(10);

		JButton btnDecrypt = new JButton("Decrypt");
		btnDecrypt.addActionListener(this::decrypt);
		btnDecrypt.setBounds(132, 98, 112, 23);
		contentPane.add(btnDecrypt);

		btnImport = new JButton("Import");
		btnImport.addActionListener(this::importConnection);
		btnImport.setEnabled(false);
		btnImport.setBounds(255, 98, 112, 23);
		contentPane.add(btnImport);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 132, 408, 98);
		contentPane.add(scrollPane);

		JPanel panel = new JPanel();
		scrollPane.setColumnHeaderView(panel);
		panel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(45dlu;default)"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(69dlu;default):grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel lblStatus = new JLabel("Status");
		panel.add(lblStatus, "2, 2, left, default");

		lblStatusValue = new JLabel("No file decrypted");
		panel.add(lblStatusValue, "4, 2, fill, default");

		JLabel lblName = new JLabel("Name");
		panel.add(lblName, "2, 4");

		lblNameValue = new JLabel("");
		panel.add(lblNameValue, "4, 4");

		JLabel lblIp = new JLabel("IP");
		panel.add(lblIp, "2, 6");

		lblIPValue = new JLabel("");
		panel.add(lblIPValue, "4, 6");

		JLabel lblPort = new JLabel("Port");
		panel.add(lblPort, "2, 8");

		lblPortValue = new JLabel("");
		panel.add(lblPortValue, "4, 8");
	}

	private void decrypt(ActionEvent e) {
		String path = fileField.getText();
		if (storage.doesFileExist(path)) {
			try {
				IPasswordEncryption encryption = factory.provide(IPasswordEncryption.class);
				encryption.init(passwordField.getPassword());
				
				
				profile = profileProvider.loadProfileFromFile(storage, path, encryption);
				
				if(profile == null){
					setStatus(false, "Failed to decrypt");
					profile = null;
				}
				
				if(profile.isTemplate()){
					setStatus(false, "Profile is not a template");
					profile = null;
					return;
				}
				
				setStatus(true, "Decrypted");
				lblNameValue.setText(profile.getName());
				lblIPValue.setText(profile.getIP());
				lblPortValue.setText(Integer.toString(profile.getPort()));
			} catch (Exception ex) {
				ex.printStackTrace();
				setStatus(false, "Failed to decrypt");
				profile = null;
			}
		} else {
			setStatus(false, "File not found");
			profile = null;
		}
	}
	
	private void setStatus(boolean ok, String text){
		lblStatusValue.setText(text);
		btnImport.setEnabled(ok);
		if(!ok){
			lblNameValue.setText("");
			lblIPValue.setText("");
			lblPortValue.setText("");
		}
	}

	private void importConnection(ActionEvent e) {
//		if (connectionInfo != null) {
//			dispose();
//			InitialConnection ic = new InitialConnection(client, connectionInfo);
//			ic.setVisible(true);
//		}
	}
}