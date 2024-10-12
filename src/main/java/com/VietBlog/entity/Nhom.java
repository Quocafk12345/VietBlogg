package com.VietBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Nhom")
public class Nhom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Nhom", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "Ten", nullable = false)
    private String ten;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "Gioi_Thieu", nullable = false)
    private String gioiThieu;

    @JsonIgnore
    @OneToMany(mappedBy = "nhom")
    private List<ThanhVien> thanhVien;

    @JsonIgnore
    @OneToMany(mappedBy = "nhom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaiViet> baiViet;
}