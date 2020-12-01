package com.chat.server;

public enum MessageTypes {
	INFO(""), IN("in: "), OUT("out: ");

	private final String value;

	MessageTypes(final String newValue) {
		value = newValue;
	}

	public String getValue() {
		return value;
	}
}
