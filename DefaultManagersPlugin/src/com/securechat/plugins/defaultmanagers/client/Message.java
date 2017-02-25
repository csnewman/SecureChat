package com.securechat.plugins.defaultmanagers.client;

import com.securechat.api.client.chat.IMessage;
import com.securechat.api.common.security.IEncryption;

public class Message implements IMessage {
	private boolean isProtected;
	private byte[] content;
	private String text, sender;
	private long time;

	public Message(byte[] content, boolean isProtected, String sender, long time) {
		this.content = content;
		this.isProtected = isProtected;
		this.text = isProtected ? "Message locked" : new String(content);
		this.sender = sender;
		this.time = time;
	}
	
	@Override
	public void unlock(IEncryption encryption) {
		if(!isProtected)
			return;
		try {
			text = new String(encryption.decrypt(content));
		} catch (Exception e) {
			e.printStackTrace();
			text = "(Message is corrupted)";
		}
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getSender() {
		return sender;
	}

	@Override
	public long getTime() {
		return time;
	}

}
