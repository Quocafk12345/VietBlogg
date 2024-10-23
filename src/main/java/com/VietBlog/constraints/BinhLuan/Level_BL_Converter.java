package com.VietBlog.constraints.BinhLuan;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class Level_BL_Converter implements AttributeConverter<Level_Binh_Luan, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Level_Binh_Luan level) {
		return level.getValue();
	}

	@Override
	public Level_Binh_Luan convertToEntityAttribute(Integer dbData) {
		return switch (dbData) {
			case 0 -> Level_Binh_Luan.BINH_LUAN_GOC;
			case 1 -> Level_Binh_Luan.CAP_1;
			case 2 -> Level_Binh_Luan.CAP_2;
			default -> throw new IllegalArgumentException("Unexpected database value: " + dbData);
		};
	}
}
