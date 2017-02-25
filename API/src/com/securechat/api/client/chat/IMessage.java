package com.securechat.api.client.chat;

import com.securechat.api.common.security.IEncryption;

public interface IMessage {

	public void unlock(IEncryption encryption);

	public String getText();

	public String getSender();

	public long getTime();

}
