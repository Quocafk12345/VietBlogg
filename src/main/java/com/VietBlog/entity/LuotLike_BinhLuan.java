package com.VietBlog.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "Luot_Like_Binh_Luan")
public class LuotLike_BinhLuan {
	@EmbeddedId
	private LuotLikeBinhLuanId id;

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "User_Id", nullable = false)
	private User user;

	@MapsId("idBinhLuan")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "Id_Binh_Luan", nullable = false)
	private BinhLuan idBinhLuan;

}