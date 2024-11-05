package com.VietBlog.constraints.User;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FontChu_Converter implements AttributeConverter<FontChu_User, String> {
	@Override
	public String convertToDatabaseColumn(FontChu_User fontChuUser) {
		return fontChuUser.getValue();
	}

	@Override
	public FontChu_User convertToEntityAttribute(String dbData) {
		return switch (dbData) {
			case "Times New Roman" -> FontChu_User.TIMES_NEW_ROMAN;
			case "Tahoma" -> FontChu_User.TAHOMA;
			case "Verdana" -> FontChu_User.VERDANA;
			case "Helvetica Neue" -> FontChu_User.HELVETICA_NEUE;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}
