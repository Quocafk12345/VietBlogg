package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ThanhVienId implements Serializable {
    @Serial
    private static final long serialVersionUID = 8103918728952252262L;
    @NotNull
    @Column(name = "Id_Nhom", nullable = false)
    private Long idNhom;

    @NotNull
    @Column(name = "User_Id", nullable = false)
    private Long userId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThanhVienId entity = (ThanhVienId) o;
        return Objects.equals(this.idNhom, entity.idNhom) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNhom, userId);
    }

    // thêm để chỉ hiện thị (idNhom, userId) cho tại run
    @Override
    public String toString() {
        return "ThanhVienId{" +
                "nhomId=" + idNhom +
                ", userId=" + userId +
                '}';
    }
}