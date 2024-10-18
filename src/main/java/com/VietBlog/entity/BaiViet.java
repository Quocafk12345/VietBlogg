package com.VietBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Bai_Viet")
public class BaiViet implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Id_Bai_Viet")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Hãy viết tiêu đề cho bài")
    @Column(name = "Tieu_De", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String tieuDe;

    @Column(name = "Thumbnail", columnDefinition = "NVARCHAR(MAX)")
    private String thumbnail;

    @NotNull
    @Nationalized
    @Lob
    @NotBlank(message = "Hãy viết nội dung cho bài")
    @Column(name = "Noi_Dung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Ngay_Tao", nullable = false)
    private Timestamp ngayTao;

    @Column(name = "Trang_Thai", length = 255, nullable = false)
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
}