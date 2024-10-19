package com.VietBlog.entity;

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
import java.time.LocalDate;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "Ngay_Tao", nullable = false)
    private LocalDate ngayTao;

    @Column(name = "Trang_Thai", nullable = false)
    private String trangThai;

    @ManyToOne
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
    private Set<DaPhuongTien> daPhuongTien = new LinkedHashSet<>();


}