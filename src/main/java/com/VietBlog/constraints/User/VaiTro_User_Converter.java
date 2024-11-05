package com.VietBlog.constraints.User;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VaiTro_User_Converter implements AttributeConverter<VaiTro_User, String> {
	@Override
	public String convertToDatabaseColumn(VaiTro_User vaiTroUser) {
		return vaiTroUser.getValue();
	}

	@Override
	public VaiTro_User convertToEntityAttribute(String dbData) {
		return switch (dbData) {
			case "ADMIN" -> VaiTro_User.ADMIN;
			case "USER" -> VaiTro_User.USER;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}
