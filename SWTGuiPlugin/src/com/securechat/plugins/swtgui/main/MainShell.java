package com.securechat.plugins.swtgui.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

public class MainShell extends Shell {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table chatsTable;
	// private Table invitesTable;
	private Table usersTable;
	private CTabFolder chatsTabFolder;

	private Label lblServerName, lblServerInfo;

	public MainShell(Display display, MainGui gui) {
		super(display, SWT.SHELL_TRIM | SWT.BORDER);
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
		lblServerInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
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

		chatsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (chatsTable.getSelectionCount() > 0) {
					gui.openChat(chatsTable.getSelectionIndex());
				}
			}
		});

		TableColumn tblclmnUnread = new TableColumn(chatsTable, SWT.NONE);
		tblclmnUnread.setWidth(70);
		tblclmnUnread.setText("Unread");

		TableColumn tblclmnChatNames = new TableColumn(chatsTable, SWT.NONE);
		tblclmnChatNames.setWidth(100);
		tblclmnChatNames.setText("Name");

		// TabItem tbtmInvites = new TabItem(leftTabFolder, SWT.NONE);
		// tbtmInvites.setText("Invites");
		//
		// invitesTable = new Table(leftTabFolder, SWT.BORDER |
		// SWT.FULL_SELECTION);
		// invitesTable.setLinesVisible(true);
		// invitesTable.setHeaderVisible(true);
		// tbtmInvites.setControl(invitesTable);
		// formToolkit.paintBordersFor(invitesTable);

		// TableColumn tblclmnInviteNames = new TableColumn(invitesTable,
		// SWT.NONE);
		// tblclmnInviteNames.setWidth(100);
		// tblclmnInviteNames.setText("Name");

		TabItem tbtmUsers = new TabItem(leftTabFolder, SWT.NONE);
		tbtmUsers.setText("Users");

		usersTable = new Table(leftTabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		usersTable.setLinesVisible(true);
		usersTable.setHeaderVisible(true);
		tbtmUsers.setControl(usersTable);
		formToolkit.paintBordersFor(usersTable);

		Menu menuTable = new Menu(usersTable);
		usersTable.setMenu(menuTable);

		MenuItem miTest = new MenuItem(menuTable, SWT.NONE);
		miTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				gui.startChat(usersTable.getSelectionIndex());
			}
		});
		miTest.setText("Open Chat");

		usersTable.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (usersTable.getSelectionCount() <= 0) {
					event.doit = false;
				}
			}
		});

		TableColumn tblclmnUsername = new TableColumn(usersTable, SWT.NONE);
		tblclmnUsername.setWidth(100);
		tblclmnUsername.setText("Username");

		TableColumn tblclmnStatus = new TableColumn(usersTable, SWT.NONE);
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");

		chatsTabFolder = new CTabFolder(sashForm, SWT.BORDER | SWT.CLOSE);
		formToolkit.adapt(chatsTabFolder);
		formToolkit.paintBordersFor(chatsTabFolder);
		chatsTabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		chatsTabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			@Override
			public void close(CTabFolderEvent event) {
				System.out.println("close " + event.item);
				// event.item

			}
		});

		sashForm.setWeights(new int[] { 20, 80 });
	}

	public FormToolkit getFormToolkit() {
		return formToolkit;
	}

	public Label getLblServerName() {
		return lblServerName;
	}

	public Label getLblServerInfo() {
		return lblServerInfo;
	}

	public Table getChatsTable() {
		return chatsTable;
	}

	public Table getUsersTable() {
		return usersTable;
	}
	
	public CTabFolder getChatsTabFolder() {
		return chatsTabFolder;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
