package com.VietBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Luot_Like_Bai_Viet")
public class LuotLike_BaiViet implements Serializable {

    @EmbeddedId
    private LuotLike_BaiViet_ID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idBaiViet")
    @JoinColumn(name = "Id_Bai_Viet", nullable = false)
    private BaiViet baiViet;

    // Constructors, getters và setters
}
