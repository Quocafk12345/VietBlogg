package com.VietBlog.entity;

import com.VietBlog.constraints.ThanhVien.VaiTro_ThanhVien;
import com.VietBlog.constraints.ThanhVien.VaiTro_ThanhVien_Converter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Thanh_Vien")
public class ThanhVien {
    @EmbeddedId
    private ThanhVienId id;

    @MapsId("idNhom")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Nhom", nullable = false)
    private Nhom nhom;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Convert(converter = VaiTro_ThanhVien_Converter.class)
    @Column(name = "Vai_Tro", nullable = false)
    private VaiTro_ThanhVien vaiTro;

    @Temporal(TemporalType.DATE)
    @Column(name = "Ngay_Tham_Gia")
    private LocalDate ngayThamGia;


}