package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BlockUserNhomId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "User_Id", nullable = false)
    private Long userId;

    @Column(name = "Blocked_User_Id", nullable = false) // Thêm cột này
    private Long blockedUserId; // Người bị chặn

    @Column(name = "Id_Nhom", nullable = false)
    private Long nhomId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockUserNhomId that = (BlockUserNhomId) o;
        return userId.equals(that.userId) && blockedUserId.equals(that.blockedUserId) && nhomId.equals(that.nhomId); // Thêm blockedUserId
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, blockedUserId, nhomId); // Thêm blockedUserId
    }
}