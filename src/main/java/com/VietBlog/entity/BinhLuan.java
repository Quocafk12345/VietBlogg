package com.VietBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

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
    @Column(name = "Level_Binh_Luan")
    private Integer level;


    @Column(name = "Noi_Dung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    /**
     * Đây là cột để ghi id của bình luận mà nó phản hồi.
     * <ul>
     *   <li>NULL: Bình luận gốc</li>
     *   <li>KHÁC: ID của bình luận được phản hồi</li>
     * </ul>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_BL_Cha")
    private BinhLuan binhLuanCha;

    @Column(name = "Ngay_Tao", nullable = false)
    private LocalDate ngayTao;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Bai_Viet", nullable = false)
    private BaiViet baiViet;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;


}
