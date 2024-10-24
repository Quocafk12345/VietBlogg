package com.VietBlog.constraints.User;

public enum MauNen_User {
	WHITE("TRẮNG"),
	BLACK("ĐEN"),
	BLUE("XANH DƯƠNG"),
	YELLOW("VÀNG"),
	DEFAULT("MẶC ĐỊNH");


	private final String value;

	MauNen_User(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}