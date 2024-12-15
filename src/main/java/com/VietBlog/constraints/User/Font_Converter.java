package com.VietBlog.constraints.User;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class Font_Converter implements AttributeConverter<Font_User, String> {
	@Override
	public String convertToDatabaseColumn(Font_User fontUser) {
		return fontUser.getValue();
	}

	@Override
	public Font_User convertToEntityAttribute(String dbData) {
		return switch (dbData) {
			case "Times New Roman" -> Font_User.TIMES_NEW_ROMAN;
			case "Tahoma" -> Font_User.TAHOMA;
			case "Verdana" -> Font_User.VERDANA;
			case "Helvetica Neue" -> Font_User.HELVETICA_NEUE;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}