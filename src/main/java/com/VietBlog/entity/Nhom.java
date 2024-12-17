package com.VietBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
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
    private Long id;

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

	@Nationalized
	@Lob
	@Column(name = "Hinh_Dai_Dien")
	private String hinhDaiDien;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Ngay_Tao", nullable = false)
	private Timestamp ngayTao;


    @Transient // Thuộc tính này sẽ không được lưu vào database
    private boolean daThamGia;

//    @Column(name = "so_luong_thanh_vien") // Thêm thuộc tính này
//    private int soLuongThanhVien;

    // Thêm Test Nhóm
    public Nhom(Long id, String ten, String gioiThieu, String hinhDaiDien) {
        this.id = id;
        this.ten = ten;
        this.gioiThieu = gioiThieu;
        this.hinhDaiDien = hinhDaiDien;
    }
    // thêm để chỉ hiện thị (id, ten) cho tại run
    @Override
    public String toString() {
        return "Nhom{" +
                "id=" + id +
                ", ten='" + ten + '\'' +
                '}';
    }
}