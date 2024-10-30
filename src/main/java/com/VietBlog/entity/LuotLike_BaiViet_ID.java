package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LuotLike_BaiViet_ID implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "User_Id")
    private Long userId;

    @Column(name = "Id_Bai_Viet")
    private Long idBaiViet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LuotLike_BaiViet_ID entity = (LuotLike_BaiViet_ID) o;
        return Objects.equals(this.idBaiViet, entity.idBaiViet) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBaiViet, userId);
    }

}

