package com.VietBlog.constraints.ThanhVien;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VaiTro_ThanhVien_Converter implements AttributeConverter<VaiTro_ThanhVien, String> {
	@Override
	public String convertToDatabaseColumn(VaiTro_ThanhVien vaiTroThanhVien) {
		return vaiTroThanhVien.getValue();
	}

	@Override
	public VaiTro_ThanhVien convertToEntityAttribute(String dbData) {
		return switch (dbData) {
			case "Chủ nhóm" -> VaiTro_ThanhVien.CHU_NHOM;
			case "Người kiểm duyệt" -> VaiTro_ThanhVien.NGUOI_KIEM_DUYET;
			case "Quản trị viên" -> VaiTro_ThanhVien.QUAN_TRI_VIEN;
			case "Thành viên" -> VaiTro_ThanhVien.THANH_VIEN;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}
