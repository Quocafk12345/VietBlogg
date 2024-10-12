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
@Table(name = "Luu_Bai_Viet")
public class LuuBaiViet implements Serializable {

    @EmbeddedId
    private LuuBaiViet_ID id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("idBaiViet")
    @JoinColumn(name = "Id_Bai_Viet", nullable = false)
    private BaiViet baiViet;

}

