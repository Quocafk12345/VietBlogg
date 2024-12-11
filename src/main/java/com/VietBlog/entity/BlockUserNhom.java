package com.VietBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Block_User_Nhom")
public class BlockUserNhom {
    @EmbeddedId
    private BlockUserNhomId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;  // Người thực hiện chặn

    @MapsId("blockedUserId") // Thêm annotation này
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Blocked_User_Id", nullable = false) // Thêm field này
    private User blockedUser; // Người bị chặn

    @MapsId("nhomId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Nhom", nullable = false)
    private Nhom nhom;

    @Column(name = "Ngay_Bat_Dau", nullable = false)
    private LocalDateTime ngayBatDau;

    @Column(name = "Ngay_Ket_Thuc")
    private LocalDateTime ngayKetThuc;
}