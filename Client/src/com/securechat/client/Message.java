package com.securechat.client;

import com.securechat.api.client.IMessage;

public class Message implements IMessage {
	private String text, sender;
	private long time;

	public Message(String text, String sender, long time) {
		this.text = text;
		this.sender = sender;
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public String getSender() {
		return sender;
	}

	public long getTime() {
		return time;
	}

}
