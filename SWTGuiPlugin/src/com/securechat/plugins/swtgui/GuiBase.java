package com.securechat.plugins.swtgui;

import java.util.concurrent.CountDownLatch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.securechat.api.client.gui.IGui;
import com.securechat.api.common.IContext;
import com.securechat.api.common.plugins.InjectInstance;

public abstract class GuiBase implements IGui {
	@InjectInstance
	protected IContext context;
	protected SWTGuiPlugin plugin;
	protected CountDownLatch closeLatch;
	protected boolean open;

	public GuiBase(SWTGuiPlugin plugin) {
		this.plugin = plugin;
		plugin.sync(new Runnable() {
			@Override
			public void run() {
				createShell();
				getShell().addListener(SWT.Close, new Listener() {
					@Override
					public void handleEvent(Event arg0) {
						open = false;
						onClose();
						closeLatch.countDown();
						closeLatch = null;
					}
				});
			}
		});
	}

	protected abstract void createShell();

	protected abstract void onOpen();

	protected abstract void onClose();

	public abstract Shell getShell();

	@Override
	public void open() {
		if (isOpen()) {
			throw new RuntimeException("Gui already open!");
		}

		open = true;

		closeLatch = new CountDownLatch(1);

		plugin.sync(new Runnable() {
			@Override
			public void run() {
				getShell().open();
				getShell().layout();
				onOpen();
			}
		});
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public void awaitClose() {
		if (closeLatch != null) {
			try {
				closeLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() {
		plugin.sync(getShell()::close);
	}

	public IContext getContext() {
		return context;
	}

}
