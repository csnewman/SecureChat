package com.securechat.plugins.basicgui;

import com.securechat.common.api.IGuiProvider;
import com.securechat.common.api.IKeystoreGui;

public class BasicGuiPlugin implements IGuiProvider {

	@Override
	public IKeystoreGui newKeystoreGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImplName() {
		return "official-basic_gui";
	}

}
