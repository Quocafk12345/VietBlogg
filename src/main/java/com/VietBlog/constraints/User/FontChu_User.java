package com.VietBlog.constraints.User;

public enum FontChu_User {
	ARIAL("Helvetica Neue"),
	TIMES_NEW_ROMAN("Times New Roman"),
	TAHOMA("Tahoma"),
	VERDANA("Verdana");

	private final String value;

	FontChu_User(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
