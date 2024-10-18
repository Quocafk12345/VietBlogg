package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class LuotLike_BinhLuan_ID implements java.io.Serializable {
	private static final long serialVersionUID = 5308043125104184209L;
	@NotNull
	@Column(name = "User_Id", nullable = false)
	private Integer userId;

	@NotNull
	@Column(name = "Id_Binh_Luan", nullable = false)
	private Integer idBinhLuan;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		LuotLike_BinhLuan_ID entity = (LuotLike_BinhLuan_ID) o;
		return Objects.equals(this.idBinhLuan, entity.idBinhLuan) &&
				Objects.equals(this.userId, entity.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idBinhLuan, userId);
	}

}