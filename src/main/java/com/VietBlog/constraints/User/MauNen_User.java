package com.VietBlog.constraints.User;

public enum MauNen_User {
	WHITE("Trắng"),
	BLACK("Đen"),
	BLUE("Xanh dương"),
	YELLOW("Vàng");

	private final String value;

	MauNen_User(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}