package com.VietBlog.constraints.User;

public enum MauNen_User {
	TRANG("Trắng"),
	DEN("Đen"),
	XANH_DUONG("Xanh dương"),
	VANG("Vàng");

	private final String value;

	MauNen_User(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}