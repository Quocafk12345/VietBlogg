package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LuotLike_BinhLuan_ID implements java.io.Serializable {
	@Serial
	private static final long serialVersionUID = 5308043125104184209L;
	@NotNull
	@Column(name = "User_Id", nullable = false)
	private Long userId;

	@NotNull
	@Column(name = "Id_Binh_Luan", nullable = false)
	private Long idBinhLuan;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LuotLike_BinhLuan_ID entity = (LuotLike_BinhLuan_ID) o;
		return Objects.equals(this.idBinhLuan, entity.idBinhLuan) &&
				Objects.equals(this.userId, entity.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idBinhLuan, userId);
	}

}