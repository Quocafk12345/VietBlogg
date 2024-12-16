package com.VietBlog.constraints.DaPhuongTien;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DaPhuongTien_Loai_Converter implements AttributeConverter<DaPhuongTien_Loai, String> {
	@Override
	public String convertToDatabaseColumn(DaPhuongTien_Loai daPhuongTien_loai) {
		return daPhuongTien_loai.getValue();
	}

	@Override
	public DaPhuongTien_Loai convertToEntityAttribute(String dbData) {
		return switch (dbData) {
			case "Hình ảnh" -> DaPhuongTien_Loai.HINH_ANH;
			case "Video" -> DaPhuongTien_Loai.VIDEO;
			case "Âm thanh" -> DaPhuongTien_Loai.AM_THANH;
			case "Khác" -> DaPhuongTien_Loai.KHAC;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}

