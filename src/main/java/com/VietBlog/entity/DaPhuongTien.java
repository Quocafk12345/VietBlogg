package com.VietBlog.entity;

import com.VietBlog.constraints.DaPhuongTien.DaPhuongTien_Loai;
import com.VietBlog.constraints.DaPhuongTien.DaPhuongTien_Loai_Converter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Da_Phuong_Tien")
public class DaPhuongTien {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id_Phuong_Tien", nullable = false)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "Id_Bai_Viet", nullable = false)
	private BaiViet baiViet;

	@NotNull
	@Nationalized
	@Lob
	@Column(name = "Duong_Dan", nullable = false)
	private String duongDan;

	@Nationalized
	@Lob
	@Column(name = "Mo_Ta")
	private String moTa;

	@Size(max = 50)
	@Nationalized
	@Column(name = "Loai", length = 50)
	@Convert(converter = DaPhuongTien_Loai_Converter.class)
	private DaPhuongTien_Loai loai;

}