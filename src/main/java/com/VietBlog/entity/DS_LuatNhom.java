package com.VietBlog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "DS_Luat_Nhom")
public class DS_LuatNhom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id_Luat", nullable = false)
	private Long id;

	@NotNull
	@Nationalized
	@Lob
	@Column(name = "Noi_Dung", nullable = false)
	private String noiDung;

	@NotNull
	@Nationalized
	@Lob
	@Column(name = "Ten", nullable = false)
	private String ten;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "Id_Nhom", nullable = false)
	private Nhom nhom;

}