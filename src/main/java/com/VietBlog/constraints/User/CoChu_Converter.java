package com.VietBlog.constraints.User;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CoChu_Converter implements AttributeConverter<CoChu_User, Integer> {
	@Override
	public Integer convertToDatabaseColumn(CoChu_User coChuUser) {
		return coChuUser.getValue();
	}

	@Override
	public CoChu_User convertToEntityAttribute(Integer dbData) {
		return switch (dbData) {
			case 16 -> CoChu_User.TRUNG_BINH;
			case 20 -> CoChu_User.LON;
			default -> CoChu_User.NHO;
		};
	}
}
