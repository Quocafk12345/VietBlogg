package com.VietBlog.entity;

import com.VietBlog.constraints.BinhLuan.Level_BL_Converter;
import com.VietBlog.constraints.BinhLuan.Level_Binh_Luan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Binh_Luan")
public class BinhLuan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Binh_Luan")
    private Long id;

    /**
     * Đây là cột để xếp thứ tự cho bình luận.
     * <ul>
     *   <li>0: Bình luận gốc</li>
     *   <li>1: Phản hồi trực tiếp của bình luận gốc</li>
     *   <li>2: Phản hồi của một phản hồi khác</li>
     * </ul>
     */
    @Convert(converter = Level_BL_Converter.class)
    @Column(name = "Level_Binh_Luan")
    private Level_Binh_Luan level;


    @Column(name = "Noi_Dung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    /**
     * Đây là cột để ghi id của bình luận mà nó phản hồi.
     * <ul>
     *   <li>NULL: Bình luận gốc</li>
     *   <li>KHÁC: ID của bình luận được phản hồi</li>
     * </ul>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Id_BL_Cha")
    private BinhLuan binhLuanCha;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Ngay_Tao", nullable = false)
    private Timestamp ngayTao;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Bai_Viet", nullable = false)
    private BaiViet baiViet;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;
}
