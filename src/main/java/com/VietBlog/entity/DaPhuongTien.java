package com.VietBlog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Da_Phuong_Tien")
public class DaPhuongTien {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id_Phuong_Tien", nullable = false)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "Id_Bai_Viet", nullable = false)
	private BaiViet baiViet;

	@NotNull
	@Nationalized
	@Lob
	@Column(name = "Duong_Dan", nullable = false)
	private String duongDan;

}