package com.VietBlog.constraints.ThanhVien;

public enum VaiTro_ThanhVien {
	CHU_NHOM("Chủ nhóm"),
	NGUOI_KIEM_DUYET("Người kiểm duyệt"),
	QUAN_TRI_VIEN("Quản trị viên"),
	THANH_VIEN("Thành viên");

	private final String value;

	VaiTro_ThanhVien(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
