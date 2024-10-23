package com.VietBlog.constraints.User;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MauNen_Converter implements AttributeConverter<MauNen_User, String> {
	@Override
	public String convertToDatabaseColumn(MauNen_User mauNenUser) {
		return mauNenUser.getValue();
	}

	@Override
	public MauNen_User convertToEntityAttribute(String dbData) {
		return switch (dbData) {
			case "Trắng" -> MauNen_User.WHITE;
			case "Đen" -> MauNen_User.BLACK;
			case "Xanh dương" -> MauNen_User.BLUE;
			case "Vàng" -> MauNen_User.YELLOW;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}
