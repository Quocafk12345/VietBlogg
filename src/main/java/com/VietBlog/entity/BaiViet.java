package com.VietBlog.entity;

import com.VietBlog.constraints.BaiViet.TrangThai_BaiViet;
import com.VietBlog.constraints.BaiViet.TrangThai_BaiViet_Converter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Bai_Viet")
public class BaiViet implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Id_Bai_Viet")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hãy viết tiêu đề cho bài")
    @Column(name = "Tieu_De", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String tieuDe;

    @NotNull
    @Nationalized
    @Lob
    @NotBlank(message = "Hãy viết nội dung cho bài")
    @Column(name = "Noi_Dung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Ngay_Tao", nullable = false)
    private Timestamp ngayTao;

    @Convert(converter = TrangThai_BaiViet_Converter.class)
    @Column(name = "Trang_Thai", nullable = false)
    private TrangThai_BaiViet trangThai;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "baiViet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BinhLuan> binhLuan;

    // Khóa ngoại cho bảng Nhom
    @ManyToOne
    @JoinColumn(name = "Id_Nhom")  // Tên cột tham chiếu theo bảng SQL Server
    private Nhom nhom; // Tên class tương ứng với bảng Nhom

    @JsonIgnore
    @OneToMany(mappedBy = "baiViet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LuuBaiViet> luuBaiViet;

    @JsonIgnore
    @OneToMany(mappedBy = "baiViet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LuotLike_BaiViet> luotLikeBaiViet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Bai_Viet_Chia_Se")
    private BaiViet baiVietChiaSe;

    @JsonIgnore
    @OneToMany(mappedBy = "baiViet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DaPhuongTien> daPhuongTien;
    // thêm để test Bài Viết
    // Constructor 4 tham số
    public BaiViet(Long id, String tieuDe, String noiDung, TrangThai_BaiViet trangThai) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.trangThai = trangThai;
    }

    // thêm để chỉ hiện thị (id, tieuDe, noiDung, trangThai) cho tại run
    @Override
    public String toString() {
        return "BaiViet{" +
                "id=" + id +
                ", tieuDe='" + tieuDe + '\'' +
                ", noiDung='" + noiDung + '\'' +
                ", trangThai=" + trangThai +
                '}';
    }
}