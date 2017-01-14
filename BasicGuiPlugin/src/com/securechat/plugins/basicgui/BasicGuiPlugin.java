package com.securechat.plugins.basicgui;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.securechat.common.IContext;
import com.securechat.common.gui.IGuiProvider;
import com.securechat.common.gui.IKeystoreGui;
import com.securechat.common.implementation.ImplementationFactory;
import com.securechat.common.plugins.Hook;
import com.securechat.common.plugins.Hooks;
import com.securechat.common.plugins.Plugin;

@Plugin(name = "official-basic_gui", version = "1.0.0")
public class BasicGuiPlugin implements IGuiProvider {
	private JFrame currentWindow;

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		ImplementationFactory factory = context.getImplementationFactory();
		factory.registerInstance("official-basic_gui", IGuiProvider.class, this);
		factory.setFallbackDefaultIfNone(IGuiProvider.class, "official-basic_gui");
	}

	@Override
	public void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IKeystoreGui newKeystoreGui() {
		return new KeystoreGui(this);
	}

	public JFrame getCurrentWindow() {
		return currentWindow;
	}

	@Override
	public String getImplName() {
		return "official-basic_gui";
	}

}
