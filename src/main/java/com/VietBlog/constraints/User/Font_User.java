package com.VietBlog.constraints.User;

public enum Font_User {
	HELVETICA_NEUE("Helvetica Neue"),
	TIMES_NEW_ROMAN("Times New Roman"),
	TAHOMA("Tahoma"),
	VERDANA("Verdana");

	private final String value;

	Font_User(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
