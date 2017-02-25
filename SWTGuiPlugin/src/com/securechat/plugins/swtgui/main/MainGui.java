package com.securechat.plugins.swtgui.main;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.securechat.api.client.IChat;
import com.securechat.api.client.IClientManager;
import com.securechat.api.client.gui.IMainGui;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.plugins.swtgui.GuiBase;
import com.securechat.plugins.swtgui.SWTGuiPlugin;

public class MainGui extends GuiBase implements IMainGui {
	public static final ImplementationMarker MARKER = new ImplementationMarker(SWTGuiPlugin.NAME, SWTGuiPlugin.VERSION,
			"main_gui", "1.0.0");
	private MainShell shell;
	private IClientManager clientManager;
	private String username, name;
	private int online, totalUsers;
	private IChat[] chats;
	private String[] usernames;
	private Map<String, ChatInstance> instances;

	public MainGui(SWTGuiPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init(IClientManager clientManager) {
		this.clientManager = clientManager;
		instances = new HashMap<String, ChatInstance>();
		name = clientManager.getConnectionProfile().getName();
		username = clientManager.getConnectionProfile().getUsername();
	}

	@Override
	protected void createShell() {
		shell = new MainShell(plugin.getDisplay(), this);
	}

	@Override
	protected void onOpen() {
		plugin.sync(() -> {
			shell.getLblServerName().setText(name);
			shell.getLblServerInfo().setText("Logged in as " + username + " (?/? online)");
		});
	}

	@Override
	public void updateOnlineCount(int count, int outOf) {
		online = count;
		totalUsers = outOf;
		plugin.sync(() -> {
			shell.getLblServerInfo()
					.setText("Logged in as " + username + " (" + online + "/" + totalUsers + " online)");
		});
	}

	@Override
	public void updateUserList(String[] usernames, boolean[] onlines) {
		plugin.sync(() -> {
			Table table = shell.getUsersTable();
			table.clearAll();

			for (int i = 0; i < usernames.length; i++) {
				TableItem item = new TableItem(table, 0);
				item.setText(new String[] { usernames[i], onlines[i] ? "Online" : "Offline" });
				item.setGrayed(!onlines[i]);
			}

			MainGui.this.usernames = usernames;
		});
	}

	@Override
	public void updateChatList(IChat[] chats) {
		plugin.sync(() -> {
			Table table = shell.getChatsTable();
			table.clearAll();

			for (IChat chat : chats) {
				TableItem item = new TableItem(table, 0);
				item.setText(new String[] { chat.isUnlocked() ? Integer.toString(chat.getUnread()) : "LOCKED",
						chat.getOtherUser() });
				item.setGrayed(!chat.isUnlocked());
			}

			MainGui.this.chats = chats;
		});
	}

	public void openChat(int index) {
		openChat(chats[index].getOtherUser());
	}

	@Override
	public void openChat(String with) {
		if (with.equals(username)) {
			return;
		}

		if (instances.containsKey(with)) {
			ChatInstance instance = instances.get(with);
			plugin.sync(() -> {
				instance.switchTo();
			});
			return;
		}

		IChat chat = clientManager.getChat(with);
		if (chat == null) {
			return;
		}
		
		while(!chat.isUnlocked()){
			InputDialog dialog = new InputDialog(shell, "Secure Chat - Unlock chat",
					"Your chat with " + with
							+  " is protected with a password. You need to enter this password to unlock this chat.",
					"", s -> s.length() == 0 ? "Too short" : null) {
				@Override
				protected int getInputTextStyle() {
					return super.getInputTextStyle() | SWT.PASSWORD;
				}
			};

			if (dialog.open() != Window.OK) {
				return;
			}
			
			chat.unlock(dialog.getValue());
		}

		ChatInstance instance = new ChatInstance(username, chat);
		instances.put(with, instance);
		plugin.sync(() -> {
			instance.createGui(shell.getFormToolkit(), shell.getChatsTabFolder());
			instance.switchTo();
			instance.updateMessages();
		});
	}

	public void startChat(int index) {
		String with = usernames[index];
		if (with.equals(username)) {
			return;
		}

		if (clientManager.doesChatExist(with)) {
			openChat(with);
			return;
		}

		InputDialog dialog = new InputDialog(shell, "Secure Chat - New chat",
				"You can protect your new chat with " + with
						+ " with a password. The other user will also have to type in this password to view the messages you have sent them.\n"
						+ "If no password is entered, the chat will be left unprotected.",
				"", this::inputValidator) {
			@Override
			public void setErrorMessage(String errorMessage) {
				super.setErrorMessage(errorMessage);
				Control button = getButton(IDialogConstants.OK_ID);
				if (button != null) {
					button.setEnabled(true);
				}
			}

			@Override
			protected int getInputTextStyle() {
				return super.getInputTextStyle() | SWT.PASSWORD;
			}
		};

		if (dialog.open() == Window.OK) {
			String pwd = dialog.getValue();
			clientManager.startChat(with, pwd.length() > 0, pwd);
		}
	}

	@Override
	public void updateMessages(String username) {
		if (instances.containsKey(username)) {
			plugin.sync(() -> {
				instances.get(username).updateMessages();
			});
		}
	}

	private String inputValidator(String text) {
		return "Your chat " + (text.length() > 0 ? "WILL" : "will NOT") + " be protected";
	}

	@Override
	protected void onClose() {
		context.exit();
	}

	@Override
	public Shell getShell() {
		return shell;
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}
}
