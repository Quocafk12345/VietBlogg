package com.VietBlog.constraints.User;

public enum VaiTro_User {
	ADMIN("Quản trị viên"),
	USER("Người dùng");

	private final String value;

	VaiTro_User(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
