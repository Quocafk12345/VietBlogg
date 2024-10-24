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
			case "TRẮNG" -> MauNen_User.WHITE;
			case "ĐEN" -> MauNen_User.BLACK;
			case "XANH DƯƠNG" -> MauNen_User.BLUE;
			case "VÀNG" -> MauNen_User.YELLOW;
			case "MẶC ĐỊNH" -> MauNen_User.DEFAULT;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}
