package com.VietBlog.constraints.User;

public enum Theme_User {
	LIGHT("SÁNG"),
	DARK("TỐI");

	private final String value;

	Theme_User(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}