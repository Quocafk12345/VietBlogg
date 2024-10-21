package com.VietBlog.constraints.User;

public enum CoChu_User {
	NHO(14),
	TRUNG_BINH(16),
	LON(20);

	private final int value;

	CoChu_User(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
