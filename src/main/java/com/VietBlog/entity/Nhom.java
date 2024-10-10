package com.VietBlog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Getter
@Setter
@Entity
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

    @OneToMany(mappedBy = "nhom")
    private List<ThanhVien> thanhVien;

}