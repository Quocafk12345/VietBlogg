package com.VietBlog.constraints.User;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class Theme_Converter implements AttributeConverter<Theme_User, String> {
	@Override
	public String convertToDatabaseColumn(Theme_User mauNenUser) {
		return mauNenUser.getValue();
	}

	@Override
	public Theme_User convertToEntityAttribute(String dbData) {
		return switch (dbData) {
			case "SANG" -> Theme_User.LIGHT;
			case "TOI" -> Theme_User.DARK;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}
