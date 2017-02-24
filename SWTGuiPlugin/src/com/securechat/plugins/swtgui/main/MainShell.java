package com.securechat.plugins.swtgui.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.securechat.plugins.swtgui.login.LoginGui;

import swing2swt.layout.BorderLayout;

public class MainShell extends Shell {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table chatsTable;
	private Table invitesTable;
	private Table usersTable;
	private Text textMessage;
	private Label lblServerName, lblServerInfo;
	

	public MainShell(Display display, MainGui gui) {
		super(display, SWT.CLOSE | SWT.TITLE);
		setText("Secure Chat");
		setSize(450, 433);
		setLayout(new BorderLayout(0, 0));

		SashForm sashForm = new SashForm(this, SWT.SMOOTH);
		sashForm.setLayoutData(BorderLayout.CENTER);

		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);

		lblServerName = new Label(composite_1, SWT.NONE);
		lblServerName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblServerName.setFont(SWTResourceManager.getFont("Ubuntu", 15, SWT.NORMAL));
		formToolkit.adapt(lblServerName, true, true);
		lblServerName.setText("...");

		lblServerInfo = new Label(composite_1, SWT.NONE);
		formToolkit.adapt(lblServerInfo, true, true);
		lblServerInfo.setText("...");

		TabFolder leftTabFolder = new TabFolder(composite, SWT.NONE);
		leftTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.adapt(leftTabFolder);
		formToolkit.paintBordersFor(leftTabFolder);

		TabItem tbtmChats = new TabItem(leftTabFolder, SWT.NONE);
		tbtmChats.setText("Chats");

		chatsTable = new Table(leftTabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmChats.setControl(chatsTable);
		formToolkit.paintBordersFor(chatsTable);
		chatsTable.setHeaderVisible(true);

		TableColumn tblclmnUnread = new TableColumn(chatsTable, SWT.NONE);
		tblclmnUnread.setWidth(70);
		tblclmnUnread.setText("Unread");

		TableColumn tblclmnChatNames = new TableColumn(chatsTable, SWT.NONE);
		tblclmnChatNames.setWidth(100);
		tblclmnChatNames.setText("Name");

		TableItem tableItem = new TableItem(chatsTable, SWT.NONE);
		tableItem.setText("12");

		TableItem tableItem_1 = new TableItem(chatsTable, 0);
		tableItem_1.setText("New TableItem");

		TableItem tableItem_2 = new TableItem(chatsTable, 0);
		tableItem_2.setText("New TableItem");

		TableItem tableItem_3 = new TableItem(chatsTable, 0);
		tableItem_3.setText("New TableItem");

		TableItem tableItem_4 = new TableItem(chatsTable, 0);
		tableItem_4.setText("New TableItem");

		TabItem tbtmInvites = new TabItem(leftTabFolder, SWT.NONE);
		tbtmInvites.setText("Invites");

		invitesTable = new Table(leftTabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		invitesTable.setLinesVisible(true);
		invitesTable.setHeaderVisible(true);
		tbtmInvites.setControl(invitesTable);
		formToolkit.paintBordersFor(invitesTable);

		TableColumn tblclmnInviteNames = new TableColumn(invitesTable, SWT.NONE);
		tblclmnInviteNames.setWidth(100);
		tblclmnInviteNames.setText("Name");

		TabItem tbtmUsers = new TabItem(leftTabFolder, SWT.NONE);
		tbtmUsers.setText("Users");

		usersTable = new Table(leftTabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		usersTable.setLinesVisible(true);
		usersTable.setHeaderVisible(true);
		tbtmUsers.setControl(usersTable);
		formToolkit.paintBordersFor(usersTable);

		TableColumn tblclmnUsername = new TableColumn(usersTable, SWT.NONE);
		tblclmnUsername.setWidth(100);
		tblclmnUsername.setText("Username");

		TableColumn tblclmnStatus = new TableColumn(usersTable, SWT.NONE);
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");

		CTabFolder chatsTabFolder = new CTabFolder(sashForm, SWT.BORDER | SWT.CLOSE);
		formToolkit.adapt(chatsTabFolder);
		formToolkit.paintBordersFor(chatsTabFolder);
		chatsTabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tbtmChat = new CTabItem(chatsTabFolder, SWT.NONE);
		tbtmChat.setShowClose(true);
		tbtmChat.setText("New Item");

		Composite tabComposite = new Composite(chatsTabFolder, SWT.NONE);
		tbtmChat.setControl(tabComposite);
		formToolkit.paintBordersFor(tabComposite);
		tabComposite.setLayout(new GridLayout(1, false));
		
		Composite messagesComposite = new Composite(tabComposite, SWT.NONE);
		messagesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Browser messagesBrowser = new Browser(messagesComposite, SWT.NONE);

		String result = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/test.html"))).lines()
				.collect(Collectors.joining("\n"));
		
		String msg = "";
		
		for(int i = 0; i < 50; i++){
			msg += "<li class=\"right\">hello</li>";
			msg += "<li>hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello hello </li>";
		}
		
		result = result.replace("@MESSAGES@", msg);
		
		messagesBrowser.setText(result);
		
		String stringToAdd = "wee";
		messagesBrowser.execute(String.format("document.write('%s')", stringToAdd
		                .replace("\\", "\\\\")
		                .replace("'", "\\'")
		                .replace("\"", "\\\"")));
		
		messagesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		formToolkit.adapt(messagesComposite);
		formToolkit.paintBordersFor(messagesComposite);

		Composite lowerComposite = new Composite(tabComposite, SWT.NONE);
		lowerComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(lowerComposite);
		formToolkit.paintBordersFor(lowerComposite);
		lowerComposite.setLayout(new GridLayout(2, false));

		textMessage = new Text(lowerComposite, SWT.BORDER);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textMessage.setBounds(0, 0, 81, 40);
		formToolkit.adapt(textMessage, true, true);

		Button btnSend = new Button(lowerComposite, SWT.NONE);
		btnSend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(btnSend, true, true);
		btnSend.setText("Send");

		sashForm.setWeights(new int[] { 20, 80 });
	}
	
	public void updateServerInfo(String msg){
		lblServerInfo.setText(msg);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
