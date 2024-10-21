package com.VietBlog.constraints;

public enum TrangThai_BaiViet {
	CHO_DUYET("Chờ duyệt"),
	DA_DANG("Đã đăng"),
	NHAP("Nháp"),
	TU_CHOI("Từ chối"),
	AN("Ẩn");

	private String value;

	TrangThai_BaiViet(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}



