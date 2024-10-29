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
			case "ĐEN" -> MauNen_User.BLACK;
			default -> MauNen_User.WHITE;

		};
	}
}
