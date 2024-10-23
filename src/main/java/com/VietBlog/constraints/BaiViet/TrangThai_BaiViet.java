package com.VietBlog.constraints.BaiViet;

public enum TrangThai_BaiViet {
	CHO_DUYET("CHỜ DUYỆT"),
	DA_DANG("ĐÃ ĐĂNG"),
	NHAP("NHÁP"),
	TU_CHOI("TỪ CHỐI"),
	AN("ẨN");

	private String value;

	TrangThai_BaiViet(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}