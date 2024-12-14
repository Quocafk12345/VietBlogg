package com.VietBlog.constraints.DaPhuongTien;

public enum DaPhuongTien_Loai {
	HINH_ANH("Hình ảnh"),
	VIDEO("Video"),
	AM_THANH("Âm thanh"),
	KHAC("Khác");

	private final String value;

	DaPhuongTien_Loai(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
