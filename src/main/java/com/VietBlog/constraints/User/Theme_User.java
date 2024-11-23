package com.VietBlog.constraints.User;

public enum Theme_User {
	LIGHT("SANG"),
	DARK("TOI");

	private final String value;

	Theme_User(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}