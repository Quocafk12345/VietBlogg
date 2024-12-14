package com.VietBlog.constraints.BaiViet;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThai_BaiViet_Converter implements AttributeConverter<TrangThai_BaiViet, String> {

	@Override
	public String convertToDatabaseColumn(TrangThai_BaiViet trangThai) {
		return trangThai.getValue();
	}

	@Override
	public TrangThai_BaiViet convertToEntityAttribute(String dbData) {
		return switch (dbData) {
			case "ĐÃ ĐĂNG" -> TrangThai_BaiViet.DA_DANG;
			case "NHÁP" -> TrangThai_BaiViet.NHAP;
			case "CHỜ DUYỆT" -> TrangThai_BaiViet.CHO_DUYET; // Chấp nhận cả hai dạng
			case "TỪ CHỐI" -> TrangThai_BaiViet.TU_CHOI;
			case "ẨN" -> TrangThai_BaiViet.AN;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}
