package com.VietBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Bai_Viet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaiViet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBaiViet;

    private String tieuDe;
    private String thumbnail;
    private String noiDung;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ngayTao;

    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "baiViet", cascade = CascadeType.ALL)
//    private List<BinhLuan> binhLuans;
}
