package com.securechat.api.common.database;

/**
 * Represents the type of field.
 */
public enum FieldType {
	/**
	 * Represents a unique field
	 */
	Primary,
	/**
	 * Represents a field that must be present
	 */
	Required,
	/**
	 * Represents a field that may or may not exist
	 */
	Optional
}
