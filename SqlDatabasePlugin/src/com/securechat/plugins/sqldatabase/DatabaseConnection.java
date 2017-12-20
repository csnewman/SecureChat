package com.securechat.plugins.sqldatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class DatabaseConnection {
	private String connectionUrl;
	private boolean closed;
	private Connection connection;
	private List<Statement> directStatements;

	public DatabaseConnection(String connectionUrl) {
		this.connectionUrl = connectionUrl;
		closed = true;
		directStatements = new LinkedList<Statement>();
		open();
	}

	public void open() {
		try {
			if (connection != null && connection.isClosed())
				closed = true;
		} catch (SQLException e) {
		}

		if (closed) {
			try {
				// Opens the connection
				connection = DriverManager.getConnection(connectionUrl);
			} catch (SQLException e) {
				throw new RuntimeException("Failed to connect to database", e);
			}
		}
	}

	public Statement createStatement() throws SQLException {
		Statement statement = connection.createStatement();
		directStatements.add(statement);
		return statement;
	}

	public PreparedStatement getStatement(String sql) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sql);
		directStatements.add(statement);
		return statement;
	}

	public PreparedStatement getStatement(String sql, int flags) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sql, flags);
		directStatements.add(statement);
		return statement;
	}

	public Connection getRawConnection() {
		return connection;
	}

	public void close() {
		// Close all open statements
		for (Statement statement : directStatements) {
			try {
				statement.close();
			} catch (SQLException e) {
			}
		}
		directStatements.clear();

		// Close the connection
		try {
			connection.close();
		} catch (SQLException e) {
		}

		closed = true;
	}

}
