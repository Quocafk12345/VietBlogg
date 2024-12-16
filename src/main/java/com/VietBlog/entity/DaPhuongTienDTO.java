package com.VietBlog.entity;

import com.VietBlog.constraints.DaPhuongTien.DaPhuongTien_Loai;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaPhuongTienDTO {
	private Long id;
	private String duongDan;
	private String moTa;
	private DaPhuongTien_Loai loai;
}
