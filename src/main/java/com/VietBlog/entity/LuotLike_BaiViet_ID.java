package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class LuotLike_BaiViet_ID implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "User_Id")
    private Long userId;

    @Column(name = "Id_Bai_Viet")
    private Long idBaiViet;
}

