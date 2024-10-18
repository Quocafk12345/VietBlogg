package com.VietBlog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Thong_Bao")
public class ThongBao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id_Thong_Bao", nullable = false)
	private Integer id;

	@NotNull
	@Nationalized
	@Lob
	@Column(name = "Noi_Dung", nullable = false)
	private String noiDung;

	@Nationalized
	@Lob
	@Column(name = "Duong_Dan")
	private String duongDan;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "User_Id", nullable = false)
	private User user;

}