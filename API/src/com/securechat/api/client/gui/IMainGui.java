package com.securechat.api.client.gui;

import com.securechat.api.client.chat.IChat;

/**
 * A main GUI instance
 */
public interface IMainGui extends IGui {

	/**
	 * Configures the GUI ready to be shown.
	 */
	public void init();

	/**
	 * Updates the user list.
	 * 
	 * @param username
	 *            the usernames to be shown
	 * @param online
	 *            whether those users are online
	 */
	public void updateUserList(String[] username, boolean[] online);

	/**
	 * Updates the chat list.
	 * 
	 * @param chats
	 *            the chats to be shown
	 */
	public void updateChatList(IChat[] chats);

	/**
	 * Updates the online user count.
	 * 
	 * @param count
	 *            the number of online users
	 * @param outOf
	 *            the total number of users on the server
	 */
	public void updateOnlineCount(int count, int outOf);

	/**
	 * Opens the chat window for the given user/
	 * 
	 * @param username
	 *            the other persons username
	 */
	public void openChat(String username);

	/**
	 * Updates the displayed messages for a given user.
	 * 
	 * @param username
	 */
	public void updateMessages(String username);

	/**
	 * Updates the unread message count for the chat.
	 * 
	 * @param chat
	 *            the target chat
	 */
	public void updateChatUnread(IChat chat);

	/**
	 * Displays the disconnected message and then exits.
	 * 
	 * @param msg
	 *            the reason for disconnection
	 */
	public void disconnected(String msg);

}
