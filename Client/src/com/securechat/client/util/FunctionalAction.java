package com.securechat.client.util;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;

public class FunctionalAction extends AbstractAction {
	private static final long serialVersionUID = 6191042201301346737L;
	private Consumer<ActionEvent> consumer;

	public FunctionalAction(String name, Consumer<ActionEvent> consumer) {
		super(name);
		this.consumer = consumer;
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		consumer.accept(paramActionEvent);
	}

}
