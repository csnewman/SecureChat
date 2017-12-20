package com.securechat.plugins.sqldatabase;

import com.securechat.api.common.IContext;
import com.securechat.api.common.Sides;
import com.securechat.api.common.database.IDatabase;
import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.plugins.Hook;
import com.securechat.api.common.plugins.Hooks;
import com.securechat.api.common.plugins.Plugin;

/**
 * An official plugin providing the reference implementations of the databases
 * with a SQL backing.
 */
@Plugin(name = SqlDatabasePlugin.NAME, version = SqlDatabasePlugin.VERSION)
public class SqlDatabasePlugin {
	public static final String NAME = "official-sql_database", VERSION = "1.0.0";
	private IImplementationFactory factory;

	@Hook(name = "init", hook = Hooks.Init)
	public void init(IContext context) {
		factory = context.getImplementationFactory();
	}

	@Hook(name = "init-server", hook = Hooks.Init, after = NAME + "/init", side = Sides.Server)
	public void initServer(IContext context) {
		// Registers the sql database implementation
		factory.register(SqlDatabase.MARKER, IDatabase.class, SqlDatabase::new);
	}
}
