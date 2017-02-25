package com.securechat.plugins.swtgui.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.securechat.api.client.IChat;
import com.securechat.api.client.IMessage;

public class ChatInstance {
	private String localUser;
	private IChat chat;
	private CTabFolder chatsTabFolder;
	private CTabItem tbtmChat;
	private Browser messagesBrowser;

	public ChatInstance(String localUser,  IChat chat) {
		this.localUser = localUser;
		this.chat = chat;
	}

	public void createGui(FormToolkit formToolkit, CTabFolder chatsTabFolder) {
		this.chatsTabFolder = chatsTabFolder;

		tbtmChat = new CTabItem(chatsTabFolder, SWT.NONE);
		tbtmChat.setShowClose(true);
		tbtmChat.setText(chat.getOtherUser());

		Composite tabComposite = new Composite(chatsTabFolder, SWT.NONE);
		tbtmChat.setControl(tabComposite);
		formToolkit.paintBordersFor(tabComposite);
		tabComposite.setLayout(new GridLayout(1, false));

		Composite messagesComposite = new Composite(tabComposite, SWT.NONE);
		messagesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		messagesBrowser = new Browser(messagesComposite, SWT.NONE);

		messagesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		formToolkit.adapt(messagesComposite);
		formToolkit.paintBordersFor(messagesComposite);

		Composite lowerComposite = new Composite(tabComposite, SWT.NONE);
		lowerComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(lowerComposite);
		formToolkit.paintBordersFor(lowerComposite);
		lowerComposite.setLayout(new GridLayout(2, false));

		Text textMessage = new Text(lowerComposite, SWT.BORDER);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textMessage.setBounds(0, 0, 81, 40);
		// formToolkit.adapt(textMessage, true, true);

		textMessage.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_RETURN) {
					chat.sendMessage(textMessage.getText());
					textMessage.setText("");
				}
			}
		});

		Button btnSend = new Button(lowerComposite, SWT.NONE);
		btnSend.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent paramSelectionEvent) {
				chat.sendMessage(textMessage.getText());
				textMessage.setText("");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
			}
		});
		
		btnSend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(btnSend, true, true);
		btnSend.setText("Send");
	}

	public void switchTo() {
		chatsTabFolder.setRedraw(true);
		chatsTabFolder.setSelection(tbtmChat);
		chatsTabFolder.showSelection();
	}

	public void updateMessages() {
		List<IMessage> messages = chat.getMessages();
		Collections.sort(messages, (o1, o2) -> Long.compare(o1.getTime(), o2.getTime()));

		String text = "";

		for (IMessage message : messages) {
			text += "<li ";
			if (message.getSender().equals(localUser))
				text += "class=\"right\"";
			text += ">" + message.getText() + "</li>";
		}
		messagesBrowser.setText(template.replace("@MESSAGES@", text));
	}

	public CTabItem getTbtmChat() {
		return tbtmChat;
	}

	private static final String template = new BufferedReader(new InputStreamReader(
			ChatInstance.class.getResourceAsStream("/com/securechat/plugins/swtgui/main/template.html"))).lines()
					.collect(Collectors.joining("\n"));

}
