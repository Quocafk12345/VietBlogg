package com.VietBlog.constraints.BinhLuan;

public enum Level_Binh_Luan {
	BINH_LUAN_GOC(0),
	CAP_1(1),
	CAP_2(2);

	private final int value;

	Level_Binh_Luan(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
